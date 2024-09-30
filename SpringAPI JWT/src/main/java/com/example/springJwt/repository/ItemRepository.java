package com.example.springJwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.springJwt.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {
    List<Item> findByCartId(Integer cartId); // Find items by cart ID
}
