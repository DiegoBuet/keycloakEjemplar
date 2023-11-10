package com.api.rest.repositories;

import com.api.rest.model.entities.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {
    // You can define any custom query methods if needed
}
