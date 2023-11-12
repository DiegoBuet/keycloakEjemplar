package com.api.rest.model.dto;

import com.api.rest.model.entities.PurchaseStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;


@Data
@Builder
public class DetailedPurchaseDTO {
    @NotNull(message = "id is required")
    private Long id;
    @NotNull(message = "clientId is required")
    private Long clientId;
    @NotNull(message = "products is required")
    private List<ProductDTO> products;
    @NotNull(message = "addressId is required")
    private Long addressId;
    @NotNull(message = "paymentMethodId is required")
    private Long paymentMethodId;
    private PurchaseStatus status;
}

