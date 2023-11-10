package com.api.rest.repositories;


import com.api.rest.model.entities.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    // Define any custom query methods if needed
}

