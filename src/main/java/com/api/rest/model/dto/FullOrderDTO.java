package com.api.rest.model.dto;

import com.api.rest.model.entities.OrderStatus;
import com.api.rest.model.entities.PaymentMethodType;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FullOrderDTO {
    private Long purchaseId;
    private Long clientId;
    private List<ProductDTO> products;
    private AddressDTO deliveryAddress;
    private Double totalAmount;
    private OrderStatus orderStatus;

    private PaymentMethodType paymentMethodType;

    private PaymentMethodType type;

    // ... Otros getters y setters

    public PaymentMethodType getType() {
        return type;
    }

    public void setType(PaymentMethodType type) {
        this.type = type;
    }

    public PaymentMethodType getPaymentMethodType() {
        return paymentMethodType;
    }

    public void setPaymentMethodType(PaymentMethodType paymentMethodType) {
        this.paymentMethodType = paymentMethodType;
    }
    // Otros campos necesarios

    // Agrega constructores, getters y setters según sea necesario
}