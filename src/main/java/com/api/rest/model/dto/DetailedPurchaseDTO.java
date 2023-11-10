package com.api.rest.model.dto;

import com.api.rest.model.entities.PurchaseStatus;
import lombok.Builder;
import lombok.Data;

import java.util.List;


@Data
@Builder
public class DetailedPurchaseDTO {
    private Long id;
    private Long clientId;
    private List<ProductDTO> products;
    private Long addressId;
    private Long paymentMethodId;
    private PurchaseStatus status;
}

