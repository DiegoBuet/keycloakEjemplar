package com.api.rest.model.dto;

import lombok.Data;

import java.util.List;


@Data
public class PurchaseDTO {
    private Long clientId;
    private List<ProductDTO> products;
    private Long addressId;
    private Long paymentMethodId;
}
