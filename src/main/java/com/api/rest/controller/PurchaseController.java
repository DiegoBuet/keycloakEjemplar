package com.api.rest.controller;

import com.api.rest.model.dto.DetailedPurchaseDTO;
import com.api.rest.model.dto.ProductDTO;
import com.api.rest.model.dto.PurchaseDTO;
import com.api.rest.model.dto.PurchaseResponseDTO;
import com.api.rest.service.PurchaseService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
            try {
                DetailedPurchaseDTO createdPurchase = purchaseService.startPurchase(purchaseDTO);
                return new ResponseEntity<>(createdPurchase, HttpStatus.CREATED);
            } catch (EntityNotFoundException e) {
                // Manejar la excepción EntityNotFoundException aquí si es necesario
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } catch (Exception e) {
                // Manejar otras excepciones aquí si es necesario
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

    @GetMapping("/{id}")
    public ResponseEntity<DetailedPurchaseDTO> getPurchase(@PathVariable Long id) {
        DetailedPurchaseDTO detailedPurchase = purchaseService.getPurchase(id);
        return new ResponseEntity<>(detailedPurchase, HttpStatus.OK);
    }

/*
    @GetMapping("/current/{clientId}")
    public ResponseEntity<List<ProductDTO>> getCurrentPurchase(@PathVariable Long clientId) {
        List<ProductDTO> currentPurchase = purchaseService.getCurrentPurchase(clientId);
        return new ResponseEntity<>(currentPurchase, HttpStatus.OK);
    }
*/

    @GetMapping("/current/{clientId}")
    public ResponseEntity<PurchaseResponseDTO> getCurrentPurchase(@PathVariable Long clientId) {
        PurchaseResponseDTO currentPurchase = purchaseService.getCurrentPurchase(clientId);
        return new ResponseEntity<>(currentPurchase, HttpStatus.OK);
    }

    @PutMapping("/{id}/change-address")
    public ResponseEntity<DetailedPurchaseDTO> changeAddress(@PathVariable Long id, @RequestParam Long addressId) {
        try {
            DetailedPurchaseDTO updatedPurchase = purchaseService.changeAddress(id, addressId);
            return new ResponseEntity<>(updatedPurchase, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}/change-payment-method")
    public ResponseEntity<DetailedPurchaseDTO> changePaymentMethod(@PathVariable Long id, @RequestParam Long paymentMethodId) {
        try {
            DetailedPurchaseDTO updatedPurchase = purchaseService.changePaymentMethod(id, paymentMethodId);
            return new ResponseEntity<>(updatedPurchase, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

/*    @PostMapping("/{id}/add-product")
    public ResponseEntity<DetailedPurchaseDTO> addProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        try {
            DetailedPurchaseDTO updatedPurchase = purchaseService.addProduct(id, productDTO);
            return new ResponseEntity<>(updatedPurchase, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }*/

    @PutMapping("/{id}/modify-product")
    public ResponseEntity<DetailedPurchaseDTO> modifyProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        try {
            DetailedPurchaseDTO updatedPurchase = purchaseService.modifyProduct(id, productDTO);
            return new ResponseEntity<>(updatedPurchase, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
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

    @GetMapping("/all-products")
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> allProducts = purchaseService.getAllProducts();
        return new ResponseEntity<>(allProducts, HttpStatus.OK);
    }

}
