package com.api.rest.controller;

import com.api.rest.model.dto.*;
import com.api.rest.model.entities.Address;
import com.api.rest.model.entities.PaymentMethodType;
import com.api.rest.model.entities.Purchase;
import com.api.rest.repositories.PurchaseRepository;
import com.api.rest.service.AddressService;
import com.api.rest.service.impl.PurchaseServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@Slf4j
public class OrderController {

    private final PurchaseController purchaseController;
    private final PaymentMethodController paymentMethodController;

    private final PurchaseServiceImpl purchaseService;

    private final AddressService addressService;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    public OrderController(PurchaseController purchaseController, PaymentMethodController paymentMethodController, PurchaseServiceImpl purchaseService, AddressService addressService) {
        this.purchaseController = purchaseController;
        this.paymentMethodController = paymentMethodController;
        this.purchaseService = purchaseService;
        this.addressService = addressService;
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

            PaymentMethodType methodType = PaymentMethodType.valueOf(paymentMethodType);


            PaymentDTO paymentDTO = new PaymentDTO();
            paymentDTO.setAmount(amount);
            paymentDTO.setPaymentMethodType(methodType);


            ResponseEntity<Object> paymentMethodResponse = paymentMethodController.updatePaymentMethod(id, paymentDTO);


            if (paymentMethodResponse.getStatusCode() == HttpStatus.OK) {

                return new ResponseEntity<>("Payment method updated successfully", HttpStatus.OK);
            } else {

                return new ResponseEntity<>("Failed to update payment method", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (IllegalArgumentException e) {

            return new ResponseEntity<>("Invalid payment method type", HttpStatus.BAD_REQUEST);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private FullOrderDTO mapToFullOrderDTO(Purchase purchase) {

        List<ProductDTO> productDTOs = purchase.getPurchaseItems().stream()
                .map(item -> purchaseService.mapToProductDTO(item, purchase.getTotalAmount()))
                .collect(Collectors.toList());


        AddressDTO addressDTO = purchaseService.mapToAddressDTO(purchase.getDeliveryAddress());


        return FullOrderDTO.builder()
                .purchaseId(purchase.getId())
                .clientId(purchase.getClient().getId())
                .products(productDTOs)
                .deliveryAddress(addressDTO)
                .totalAmount(purchase.getTotalAmount().doubleValue())
                .orderStatus(purchase.getStatus())
                .build();
    }


    private AddressDTO mapToAddressDTO(Address address) {
        return AddressDTO.builder()
                .street(address.getStreet())
                .city(address.getCity())
                .state(address.getState())
                .zipCode(address.getZipCode())
                .build();
    }


    private FullOrderDTO mapToFullOrderDTO(Purchase purchase, PurchaseResponseDTO purchaseResponseDTO) {
        // Mapea los productos
        List<ProductDTO> productDTOs = purchase.getPurchaseItems().stream()
                .map(item -> purchaseService.mapToProductDTO(item, purchase.getTotalAmount()))
                .collect(Collectors.toList());


        AddressDTO addressDTO = purchaseService.mapToAddressDTO(purchase.getDeliveryAddress());


        PaymentMethodType paymentMethodType = purchase.getPaymentMethod().getType();


        return FullOrderDTO.builder()
                .purchaseId(purchaseResponseDTO.getPurchaseId())
                .clientId(purchaseResponseDTO.getClientId())
                .products(productDTOs)
                .deliveryAddress(addressDTO)
                .totalAmount(purchase.getTotalAmount().doubleValue())
                .orderStatus(purchase.getStatus())
                .paymentMethodType(paymentMethodType)
                .build();
    }


}
