package com.api.rest.repositories;

import com.api.rest.model.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    // Métodos específicos del repositorio, si es necesario
}
