package com.api.rest.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductDTO {
    private Long productId;
    private int quantity;
}

