package com.api.rest.controller;

import com.api.rest.model.dto.*;
import com.api.rest.model.entities.Address;
import com.api.rest.service.impl.PurchaseServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.function.BiFunction;

@RestController
@RequestMapping("/api/purchases")
@Slf4j
public class PurchaseController {

    private final PurchaseServiceImpl purchaseService;
    private final AddressController addressController;
    private final PaymentMethodController paymentMethodController;

    @Autowired
    public PurchaseController(PurchaseServiceImpl purchaseService, AddressController addressController, PaymentMethodController paymentMethodController) {
        this.purchaseService = purchaseService;
        this.addressController = addressController;
        this.paymentMethodController = paymentMethodController;
    }


        @PostMapping("/start")
        public ResponseEntity<DetailedPurchaseDTO> startPurchase(@RequestBody PurchaseDTO purchaseDTO) {
            try {
                DetailedPurchaseDTO createdPurchase = purchaseService.startPurchase(purchaseDTO);
                return new ResponseEntity<>(createdPurchase, HttpStatus.CREATED);
            } catch (EntityNotFoundException e) {

                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } catch (Exception e) {

                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    @GetMapping("/{id}")
    public ResponseEntity<DetailedPurchaseDTO> getPurchase(@PathVariable Long id) {
        DetailedPurchaseDTO detailedPurchase = purchaseService.getPurchase(id);
        return new ResponseEntity<>(detailedPurchase, HttpStatus.OK);
    }

    @GetMapping("/current/{clientId}")
    public ResponseEntity<PurchaseResponseDTO> getCurrentPurchase(@PathVariable Long clientId) {
        PurchaseResponseDTO currentPurchase = purchaseService.getCurrentPurchase(clientId);
        return new ResponseEntity<>(currentPurchase, HttpStatus.OK);
    }

    @DeleteMapping("/{id}/remove-product")
    public ResponseEntity<Void> removeProduct(@PathVariable Long id, @RequestParam Long productId) {
        try {
            purchaseService.removeProduct(id, productId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/{id}/add-product")
    public ResponseEntity<DetailedPurchaseDTO> addProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        try {
            DetailedPurchaseDTO updatedPurchase = purchaseService.addProduct(id, productDTO);
            return new ResponseEntity<>(updatedPurchase, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("/{id}/modify-product")
    public ResponseEntity<DetailedPurchaseDTO> modifyProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        try {
            DetailedPurchaseDTO updatedPurchase = purchaseService.modifyProduct(id, productDTO);
            return new ResponseEntity<>(updatedPurchase, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            log.error("Entity not found: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Internal server error: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all-products")
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> allProducts = purchaseService.getAllProducts();
        return new ResponseEntity<>(allProducts, HttpStatus.OK);
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<DetailedPurchaseDTO> completePurchase(@PathVariable Long id) {
        try {
            DetailedPurchaseDTO completedPurchase = purchaseService.completePurchase(id);
            return new ResponseEntity<>(completedPurchase, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<DetailedPurchaseDTO> handlePurchaseServiceMethod(
            Long id, Long paramId, BiFunction<Long, Long, DetailedPurchaseDTO> serviceMethod) {
        try {
            DetailedPurchaseDTO updatedPurchase = serviceMethod.apply(id, paramId);
            return new ResponseEntity<>(updatedPurchase, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/{id}/change-address")
    public ResponseEntity<Address> changeAddress(@PathVariable Long id, @RequestBody AddressDTO newAddressDTO) {
        try {

            return addressController.updateAddress(id, newAddressDTO);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




}