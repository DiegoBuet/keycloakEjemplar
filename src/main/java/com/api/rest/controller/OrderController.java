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

    private FullOrderDTO mapToFullOrderDTO(Purchase purchase) {
        // Mapea los productos
        List<ProductDTO> productDTOs = purchase.getPurchaseItems().stream()
                .map(item -> purchaseService.mapToProductDTO(item, purchase.getTotalAmount()))
                .collect(Collectors.toList());

        // Mapea la dirección de entrega
        AddressDTO addressDTO = purchaseService.mapToAddressDTO(purchase.getDeliveryAddress());

        // Construye el DTO completo
        return FullOrderDTO.builder()
                .purchaseId(purchase.getId())
                .clientId(purchase.getClient().getId())
                .products(productDTOs)
                .deliveryAddress(addressDTO)
                .totalAmount(purchase.getTotalAmount().doubleValue())
                .orderStatus(purchase.getStatus())
                .build();
    }

    // Método para mapear una dirección a un DTO
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

        // Mapea la dirección de entrega
        AddressDTO addressDTO = purchaseService.mapToAddressDTO(purchase.getDeliveryAddress());

        // Mapea el método de pago (Enum)
        PaymentMethodType paymentMethodType = purchase.getPaymentMethod().getType();

        // Construye el DTO completo
        return FullOrderDTO.builder()
                .purchaseId(purchaseResponseDTO.getPurchaseId())
                .clientId(purchaseResponseDTO.getClientId())
                .products(productDTOs)
                .deliveryAddress(addressDTO)
                .totalAmount(purchase.getTotalAmount().doubleValue())
                .orderStatus(purchase.getStatus())
                .paymentMethodType(paymentMethodType)  // Utiliza el enum directamente
                .build();
    }



   /* @GetMapping("/{id}")
    public ResponseEntity<FullOrderDTO> getOrder(@PathVariable Long id) {
        try {
            // Obtén la compra con todos los datos relacionados
            Purchase purchase = purchaseRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Compra no encontrada"));

            // Cargar las relaciones necesarias para evitar consultas adicionales
            purchase.getPurchaseItems().forEach(item -> item.getProduct().getName());
            purchase.getClient().getUsername();  // Ajusta según tus necesidades

            // Asegúrate de que la dirección de entrega no sea nula antes de intentar acceder a sus propiedades
            Address deliveryAddress = purchase.getDeliveryAddress();
            if (deliveryAddress == null) {
                throw new IllegalStateException("La dirección de entrega no puede ser nula");
            }

            // Asegúrate de que el totalAmount no sea nulo
            BigDecimal totalAmount = purchase.getTotalAmount();
            if (totalAmount == null) {
                throw new IllegalStateException("El totalAmount no puede ser nulo");
            }

            // Mapea la compra a un DTO que contiene todos los datos necesarios
            FullOrderDTO fullOrderDTO = mapToFullOrderDTO(purchase);

            return new ResponseEntity<>(fullOrderDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }*/

/*    @GetMapping("/{id}")
    public FullOrderDTO getOrder(@PathVariable Long id) {
        // Lógica para obtener una compra por ID utilizando PurchaseService
        DetailedPurchaseDTO detailedPurchase = purchaseService.getPurchase(id);

        // Utiliza addressService para obtener y mapear la dirección según tus necesidades
        AddressDTO addressDTO = addressService.mapToAddressDTO(detailedPurchase.getDeliveryAddress());

        // Mapea el resto de los datos a tu FullOrderDTO
        FullOrderDTO fullOrderDTO = new FullOrderDTO();
        fullOrderDTO.setPurchaseId(detailedPurchase.getId());
        fullOrderDTO.setClientId(detailedPurchase.getClientId());
        fullOrderDTO.setProducts(detailedPurchase.getProducts());
        fullOrderDTO.setDeliveryAddress(addressDTO);
        fullOrderDTO.setTotalAmount(detailedPurchase.getTotalAmount());
        fullOrderDTO.setOrderStatus(detailedPurchase.getStatus());

        // Devuelve el FullOrderDTO
        return fullOrderDTO;
    }*/

}
