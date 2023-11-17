package com.api.rest.model.dto;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class OrderRequestDTO {
    private AddressDTO address;
    private PurchaseDTO purchase;
    private PaymentDTO payment;
}
