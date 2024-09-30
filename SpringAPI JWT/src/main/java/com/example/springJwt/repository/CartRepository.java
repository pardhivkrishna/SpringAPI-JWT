package com.example.springJwt.repository;

import com.example.springJwt.model.Cart;
import com.example.springJwt.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    Optional<Cart> findByUser(User user); // Method to find a cart by user
}
