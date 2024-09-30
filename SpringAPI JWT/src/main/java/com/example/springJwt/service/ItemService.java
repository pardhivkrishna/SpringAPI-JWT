package com.example.springJwt.service;

import com.example.springJwt.model.Item;
import com.example.springJwt.repository.ItemRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    // Endpoint to get all items (accessible to both users and admins)
    @GetMapping
    public ResponseEntity<List<Item>> getAllItems() {
        List<Item> items = itemRepository.findAll();
        return ResponseEntity.ok(items);
    }

    // Endpoint to add a new item (only admins can access)
    @PostMapping
    //@PreAuthorize("hasRole('ADMIN')") // Restrict access to admins
    public ResponseEntity<Item> addItem(@RequestBody Item item) {
        Item newItem = new Item(item.getName(), item.getPrice(), item.getQuantity());
        newItem = itemRepository.save(newItem);
        return ResponseEntity.status(201).body(newItem);
    }
}
