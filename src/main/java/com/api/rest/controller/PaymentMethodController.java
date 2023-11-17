package com.api.rest.controller;

import com.api.rest.model.dto.PaymentDTO;
import com.api.rest.model.entities.PaymentMethod;

import com.api.rest.model.entities.PaymentMethodType;
import com.api.rest.service.PaymentMethodService;
import com.api.rest.util.ResponseUtil;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/payment-method")
public class PaymentMethodController {


    private final PaymentMethodService paymentMethodService;

    @Autowired
    public PaymentMethodController(PaymentMethodService paymentMethodService) {
        this.paymentMethodService = paymentMethodService;
    }

/*    @GetMapping("/{id}")
    public ResponseEntity<PaymentMethod> getPaymentMethod(@PathVariable Long id) {
        try {
            PaymentMethod paymentMethod = paymentMethodService.getPaymentMethodById(id);
            return ResponseEnatity.ok(paymentMethod);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }*/
/*
    @PutMapping("/{id}")
    public ResponseEntity<Object> updatePaymentMethod(@PathVariable Long id, @RequestBody PaymentDTO paymentDTO) {
        try {
            PaymentMethod paymentMethod = paymentMethodService.getPaymentMethodById(id);
            paymentMethod.setPaymentMethodType(paymentDTO.getPaymentMethodType());  // Asigna directamente el valor del enum
            PaymentMethod updatedPaymentMethod = paymentMethodService.updatePaymentMethod(paymentMethod);
            return ResponseEntity.ok(updatedPaymentMethod);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            // Manejar el caso en que el valor del enum no sea válido
            return ResponseUtil.buildErrorResponse(HttpStatus.BAD_REQUEST, "Tipo de método de pago no válido");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }*/
    @GetMapping("/types")
    public ResponseEntity<List<String>> getPaymentMethodTypes() {
        List<String> paymentMethodTypes = Arrays.stream(PaymentMethodType.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        return ResponseEntity.ok(paymentMethodTypes);
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
/*

    @PutMapping("/{id}")
    public ResponseEntity<Object> updatePaymentMethod(@PathVariable Long id, @RequestBody PaymentDTO paymentDTO) {
        try {
            PaymentMethod paymentMethod = paymentMethodService.getPaymentMethodById(id);
            paymentMethod.setPaymentMethodType(paymentDTO.getPaymentMethodType());
            PaymentMethod updatedPaymentMethod = paymentMethodService.updatePaymentMethod(paymentMethod);
            return ResponseEntity.ok(updatedPaymentMethod);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseUtil.buildErrorResponse(HttpStatus.BAD_REQUEST, "Tipo de método de pago no válido");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
*/

    @PutMapping("/{id}")
    public ResponseEntity<Object> updatePaymentMethod(@PathVariable Long id, @RequestBody PaymentDTO paymentDTO) {
        try {
            PaymentMethod paymentMethod = paymentMethodService.getPaymentMethodById(id);
            paymentMethod.setPaymentMethodType(paymentDTO.getPaymentMethodType());
            PaymentMethod updatedPaymentMethod = paymentMethodService.updatePaymentMethod(paymentMethod);
            return ResponseEntity.ok(updatedPaymentMethod);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseUtil.buildErrorResponse(HttpStatus.BAD_REQUEST, "Tipo de método de pago no válido");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



}


