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
    private int quantity;  // Nueva propiedad para la cantidad
    private String productName;  // Agregado para el nombre del producto
    private BigDecimal productPrice;  // Agregado para el precio del producto
    private Double totalAmount;
}

