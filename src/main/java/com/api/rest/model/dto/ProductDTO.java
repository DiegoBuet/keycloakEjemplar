package com.api.rest.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ProductDTO {
    @NotNull(message = "productId is required")
    private Long productId;
    private int quantity;
    private String productName;
    private BigDecimal productPrice;
    private Double totalAmount;
}

