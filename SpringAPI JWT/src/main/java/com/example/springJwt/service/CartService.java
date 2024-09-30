package com.example.springJwt.service;

import com.example.springJwt.model.Cart;
import com.example.springJwt.model.Item;
import com.example.springJwt.model.User;
import com.example.springJwt.repository.CartRepository;
import com.example.springJwt.repository.ItemRepository;
import com.example.springJwt.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartService {

    private final CartRepository cartRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public CartService(CartRepository cartRepository, ItemRepository itemRepository, UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    // Fetches the user's cart or creates a new one if it doesn't exist
    public Cart getUserCart() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Get the username from the authentication

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return cartRepository.findByUser(user)
                .orElseGet(() -> createCartForUser(user)); // Create a new cart if none exists
    }

    // Creates a new cart for the user
    private Cart createCartForUser(User user) {
        Cart cart = new Cart(user); // Create a new cart associated with the user
        return cartRepository.save(cart); // Save and return the new cart
    }

    // Adds an item to the user's cart with a specified quantity
    @PostMapping("/items/{itemId}")
    public ResponseEntity<Cart> addItemToCart(@PathVariable Integer itemId, @RequestParam Integer quantity) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Get the username from the authentication

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Cart cart = getUserCart(); // Get or create the user's cart

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        // Check if the requested quantity is available
        if (quantity > item.getQuantity()) {
            throw new RuntimeException("Not enough quantity available for item: " + item.getName());
        }

        // Add the item to the cart's item list
        for (int i = 0; i < quantity; i++) {
            cart.getItems().add(item); // Add the item to the cart's item list
        }

        item.setQuantity(item.getQuantity() - quantity); // Decrease the available quantity of the item
        item.setCart(cart); // Associate the item with the cart

        cartRepository.save(cart); // Save the cart with the new items
        itemRepository.save(item); // Save the updated item
        return ResponseEntity.ok(cart); // Return the updated cart
    }

    // Returns items currently in the user's cart
    @GetMapping
    public ResponseEntity<List<Item>> getItemsInCart() {
        Cart cart = getUserCart(); // Fetch the user's cart
        return ResponseEntity.ok(cart.getItems()); // Return items in the user's cart
    }
}
