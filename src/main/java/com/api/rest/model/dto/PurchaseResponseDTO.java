package com.api.rest.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PurchaseResponseDTO {
    private Long purchaseId;
    private Long clientId;
    private List<ProductResponseDTO> products;
    private Long addressId;
    private Long paymentMethodId;
    private double totalAmount;


}
