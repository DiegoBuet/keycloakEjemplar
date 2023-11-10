package com.api.rest.repositories;

import com.api.rest.model.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
    // You can define custom query methods here if needed
}
