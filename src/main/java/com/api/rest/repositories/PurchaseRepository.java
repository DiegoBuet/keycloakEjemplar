package com.api.rest.repositories;


import com.api.rest.model.entities.Client;
import com.api.rest.model.entities.Purchase;
import com.api.rest.model.entities.PurchaseStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    Optional<Purchase> findFirstByClientAndStatus(Client client, PurchaseStatus status);
}

