package com.api.rest.repositories;

import com.api.rest.model.entities.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    // Métodos específicos del repositorio, si es necesario
}
