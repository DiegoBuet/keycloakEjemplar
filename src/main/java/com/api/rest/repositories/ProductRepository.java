package com.api.rest.repositories;

import com.api.rest.model.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // You can define custom query methods here if needed
}
