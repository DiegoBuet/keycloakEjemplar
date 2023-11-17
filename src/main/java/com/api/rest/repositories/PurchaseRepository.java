package com.api.rest.repositories;


import com.api.rest.model.entities.Client;
import com.api.rest.model.entities.Purchase;
import com.api.rest.model.entities.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    Optional<Purchase> findFirstByClientAndStatus(Client client, OrderStatus status);
}

