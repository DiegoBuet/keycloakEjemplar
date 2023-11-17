package com.api.rest.controller;

import com.api.rest.model.dto.*;
import com.api.rest.model.entities.Address;
import com.api.rest.model.entities.PaymentMethodType;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@Slf4j
public class OrderController {

    private final PurchaseController purchaseController;
    private final PaymentMethodController paymentMethodController;

    @Autowired
    public OrderController(PurchaseController purchaseController, PaymentMethodController paymentMethodController) {
        this.purchaseController = purchaseController;
        this.paymentMethodController = paymentMethodController;
    }

    @PostMapping("/start")
    public ResponseEntity<DetailedPurchaseDTO> startOrder(@RequestBody PurchaseDTO purchaseDTO) {
        try {
            DetailedPurchaseDTO createdOrder = purchaseController.startPurchase(purchaseDTO).getBody();
            return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetailedPurchaseDTO> getOrder(@PathVariable Long id) {
        return purchaseController.getPurchase(id);
    }

    @GetMapping("/current/{clientId}")
    public ResponseEntity<PurchaseResponseDTO> getCurrentOrder(@PathVariable Long clientId) {
        return purchaseController.getCurrentPurchase(clientId);
    }

    @DeleteMapping("/{id}/remove-product")
    public ResponseEntity<Void> removeProductFromOrder(@PathVariable Long id, @RequestParam Long productId) {
        return purchaseController.removeProduct(id, productId);
    }

    @PostMapping("/{id}/add-product")
    public ResponseEntity<DetailedPurchaseDTO> addProductToOrder(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        return purchaseController.addProduct(id, productDTO);
    }

    @PutMapping("/{id}/modify-product")
    public ResponseEntity<DetailedPurchaseDTO> modifyProductInOrder(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        return purchaseController.modifyProduct(id, productDTO);
    }

    @GetMapping("/all-products")
    public ResponseEntity<List<ProductDTO>> getAllProductsInOrder() {
        return purchaseController.getAllProducts();
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<DetailedPurchaseDTO> completeOrder(@PathVariable Long id) {
        return purchaseController.completePurchase(id);
    }

    @PutMapping("/{id}/change-address")
    public ResponseEntity<Address> changeAddressInOrder(@PathVariable Long id, @RequestBody AddressDTO newAddressDTO) {
        return purchaseController.changeAddress(id, newAddressDTO);
    }

    @GetMapping("/payment-methods/types")
    public ResponseEntity<List<String>> getPaymentMethodTypes() {
        return paymentMethodController.getPaymentMethodTypes();
    }

    @PutMapping("/{id}/change-payment-method")
    public ResponseEntity<Object> changePaymentMethodInOrder(
            @PathVariable Long id, @RequestParam BigDecimal amount, @RequestParam String paymentMethodType) {
        try {
            // Convertir el String a PaymentMethodType
            PaymentMethodType methodType = PaymentMethodType.valueOf(paymentMethodType);

            // Construir un objeto PaymentDTO con los datos proporcionados
            PaymentDTO paymentDTO = new PaymentDTO();
            paymentDTO.setAmount(amount);
            paymentDTO.setPaymentMethodType(methodType);

            // Reutilizar la lógica de PaymentMethodController
            ResponseEntity<Object> paymentMethodResponse = paymentMethodController.updatePaymentMethod(id, paymentDTO);

            // Aquí puedes procesar el resultado según tus necesidades
            if (paymentMethodResponse.getStatusCode() == HttpStatus.OK) {
                // El método de pago se actualizó correctamente, puedes realizar acciones adicionales si es necesario
                // ...
                return new ResponseEntity<>("Payment method updated successfully", HttpStatus.OK);
            } else {
                // Manejar otros casos según tus necesidades
                return new ResponseEntity<>("Failed to update payment method", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (IllegalArgumentException e) {
            // Manejar el caso en que el valor del enum no sea válido
            return new ResponseEntity<>("Invalid payment method type", HttpStatus.BAD_REQUEST);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }





}
