package com.api.rest.service.impl;

import com.api.rest.model.entities.PaymentMethod;
import com.api.rest.repositories.PaymentMethodRepository;
import com.api.rest.service.PaymentMethodService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PaymentMethodServiceImpl implements PaymentMethodService {


    private final PaymentMethodRepository paymentMethodRepository;

    @Autowired
    public PaymentMethodServiceImpl(PaymentMethodRepository paymentMethodRepository) {
        this.paymentMethodRepository = paymentMethodRepository;
    }

    @Override
    public PaymentMethod getPaymentMethodById(Long id) {
        return paymentMethodRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Método de pago no encontrado con ID: " + id));
    }

    @Override
    public PaymentMethod updatePaymentMethod(PaymentMethod paymentMethod) {
        return paymentMethodRepository.save(paymentMethod);
    }

}
