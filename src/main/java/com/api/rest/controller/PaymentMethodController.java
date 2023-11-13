package com.api.rest.controller;

import com.api.rest.model.entities.PaymentMethod;
import com.api.rest.repositories.PaymentMethodRepository;
import com.api.rest.service.PaymentMethodService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment-method")
public class PaymentMethodController {

    private final PaymentMethodService paymentMethodService;

    @Autowired
    public PaymentMethodController(PaymentMethodService paymentMethodService) {
        this.paymentMethodService = paymentMethodService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentMethod> getPaymentMethod(@PathVariable Long id) {
        try {
            PaymentMethod paymentMethod = paymentMethodService.getPaymentMethodById(id);
            return ResponseEntity.ok(paymentMethod);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

