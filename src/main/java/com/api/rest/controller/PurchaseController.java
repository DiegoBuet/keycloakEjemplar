package com.api.rest.controller;

import com.api.rest.model.dto.DetailedPurchaseDTO;
import com.api.rest.model.dto.ProductDTO;
import com.api.rest.model.dto.PurchaseDTO;
import com.api.rest.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/purchases")
public class PurchaseController {

    private final PurchaseService purchaseService;

    @Autowired
    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @PostMapping("/start")
    public ResponseEntity<DetailedPurchaseDTO> startPurchase(@RequestBody PurchaseDTO purchaseDTO) {
        DetailedPurchaseDTO createdPurchase = purchaseService.startPurchase(purchaseDTO);
        return new ResponseEntity<>(createdPurchase, HttpStatus.CREATED);
    }

    @PostMapping("/{id}/add-product")
    public ResponseEntity<DetailedPurchaseDTO> addProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        DetailedPurchaseDTO updatedPurchase = purchaseService.addProduct(id, productDTO);
        return new ResponseEntity<>(updatedPurchase, HttpStatus.OK);
    }

    @PutMapping("/{id}/modify-product")
    public ResponseEntity<DetailedPurchaseDTO> modifyProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        DetailedPurchaseDTO updatedPurchase = purchaseService.modifyProduct(id, productDTO);
        return new ResponseEntity<>(updatedPurchase, HttpStatus.OK);
    }


    @DeleteMapping("/{id}/remove-product")
    public ResponseEntity<Void> removeProduct(@PathVariable Long id, @RequestParam Long productId) {
        purchaseService.removeProduct(id, productId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @PutMapping("/{id}/change-address")
    public ResponseEntity<DetailedPurchaseDTO> changeAddress(@PathVariable Long id, @RequestParam Long addressId) {
        DetailedPurchaseDTO updatedPurchase = purchaseService.changeAddress(id, addressId);
        return new ResponseEntity<>(updatedPurchase, HttpStatus.OK);
    }

    @PutMapping("/{id}/change-payment-method")
    public ResponseEntity<DetailedPurchaseDTO> changePaymentMethod(@PathVariable Long id, @RequestParam Long paymentMethodId) {
        DetailedPurchaseDTO updatedPurchase = purchaseService.changePaymentMethod(id, paymentMethodId);
        return new ResponseEntity<>(updatedPurchase, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetailedPurchaseDTO> getPurchase(@PathVariable Long id) {
        DetailedPurchaseDTO detailedPurchase = purchaseService.getPurchase(id);
        return new ResponseEntity<>(detailedPurchase, HttpStatus.OK);
    }


}
