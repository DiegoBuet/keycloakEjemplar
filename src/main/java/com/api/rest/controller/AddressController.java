package com.api.rest.controller;
import com.api.rest.model.dto.AddressDTO;
import com.api.rest.model.entities.Address;
import com.api.rest.service.AddressService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/addresses")
@Slf4j
public class AddressController {
    private final AddressService addressService;

    @Autowired
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping("/{addressId}")
    public ResponseEntity<Address> getAddressById(@PathVariable Long addressId) {
        try {
            Address address = addressService.getAddressById(addressId);
            return new ResponseEntity<>(address, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<Address> updateAddress(@PathVariable Long addressId, @RequestBody AddressDTO updatedAddressDTO) {
        try {
            Address existingAddress = addressService.getAddressById(addressId);
            Address updatedAddress = addressService.mapToAddress(updatedAddressDTO);

            // Actualiza las propiedades de la dirección existente con los datos actualizados
            existingAddress.setStreet(updatedAddress.getStreet());
            existingAddress.setCity(updatedAddress.getCity());
            existingAddress.setState(updatedAddress.getState());
            existingAddress.setZipCode(updatedAddress.getZipCode());

            // Guarda la dirección actualizada en la base de datos
            Address savedAddress = addressService.updateAddress(existingAddress);

            return new ResponseEntity<>(savedAddress, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            log.error("Error updating address: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Error updating address: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
