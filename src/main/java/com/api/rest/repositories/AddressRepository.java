package com.api.rest.repositories;

import com.api.rest.model.entities.Address;
import com.api.rest.model.entities.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {
    Optional<Address> findByStreetAndCityAndStateAndZipCode(
            String street, String city, String state, String zipCode);

}
