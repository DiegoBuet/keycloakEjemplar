package com.api.rest.model.dto;

import com.api.rest.model.entities.PaymentMethodType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentDTO {

    private BigDecimal amount;

    private PaymentMethodType paymentMethodType;
}
