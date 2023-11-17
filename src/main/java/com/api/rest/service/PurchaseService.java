package com.api.rest.service;

import com.api.rest.model.dto.*;
import java.util.List;
import com.api.rest.model.dto.DetailedPurchaseDTO;
import com.api.rest.model.dto.ProductDTO;
import com.api.rest.model.dto.PurchaseDTO;


public interface PurchaseService {
    DetailedPurchaseDTO startPurchase(PurchaseDTO purchaseDTO);
    DetailedPurchaseDTO getPurchase(Long id);
    PurchaseResponseDTO getCurrentPurchase(Long clientId);
    void removeProduct(Long id, Long productId);
    DetailedPurchaseDTO addProduct(Long id, ProductDTO productDTO);
    DetailedPurchaseDTO modifyProduct(Long id, ProductDTO productDTO);
    List<ProductDTO> getAllProducts();
    DetailedPurchaseDTO completePurchase(Long id);
}

