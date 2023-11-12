package com.api.rest.model.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ProductResponseDTO {
    private Long productId;
    private String productName;
    private BigDecimal productPrice;
    private int quantity;


}