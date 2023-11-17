package com.api.rest.service;

import com.api.rest.model.entities.PaymentMethod;
import jakarta.persistence.EntityNotFoundException;

public interface PaymentMethodService {
    PaymentMethod getPaymentMethodById(Long id);
    PaymentMethod updatePaymentMethod(PaymentMethod paymentMethod);
}

