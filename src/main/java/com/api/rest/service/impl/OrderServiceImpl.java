package com.api.rest.service.impl;

import com.api.rest.model.dto.*;
import com.api.rest.model.entities.Address;
import com.api.rest.model.entities.PaymentMethod;
import com.api.rest.model.entities.PaymentMethodType;
import com.api.rest.repositories.AddressRepository;
        import com.api.rest.repositories.OrderRepository;
import com.api.rest.service.AddressService;
import com.api.rest.service.OrderService;
import com.api.rest.service.PaymentMethodService;
import com.api.rest.service.PurchaseService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Override
    public DetailedPurchaseDTO createOrder(OrderRequestDTO orderRequestDTO) {
        return null;
    }

 /*   private final OrderRepository orderRepository;

    private final AddressRepository addressRepository;

    private final AddressService addressService;
    private final PaymentMethodService paymentMethodService;
    private final PurchaseService purchaseService;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, AddressRepository addressRepository, AddressService addressService, PaymentMethodService paymentMethodService, PurchaseService purchaseService) {
        this.orderRepository = orderRepository;
        this.addressRepository = addressRepository;
        this.addressService = addressService;
        this.paymentMethodService = paymentMethodService;
        this.purchaseService = purchaseService;
    }



    @Override
    public DetailedPurchaseDTO createOrder(OrderRequestDTO orderRequestDTO) {
        try {
            // Crear una dirección
            AddressDTO addressDTO = orderRequestDTO.getAddress();
            Address createdAddress = addressService.createAddress(addressDTO);

            // Crear un método de pago
            PaymentDTO paymentDTO = orderRequestDTO.getPayment();
            PaymentMethodType paymentMethodType = paymentDTO.getPaymentMethodType();
            PaymentMethod paymentMethod = new PaymentMethod();
            paymentMethod.setPaymentMethodType(paymentMethodType);
            PaymentMethod createdPaymentMethod = paymentMethodService.updatePaymentMethod(paymentMethod);

            // Iniciar una nueva compra
            PurchaseDTO purchaseDTO = orderRequestDTO.getPurchase();
            purchaseDTO.setAddressId(createdAddress.getId());
            purchaseDTO.setPaymentMethodId(createdPaymentMethod.getId());
            DetailedPurchaseDTO createdOrder = purchaseService.startPurchase(purchaseDTO);

            // Otras operaciones según sea necesario

            return createdOrder;
        } catch (EntityNotFoundException e) {
            // Manejar la excepción EntityNotFoundException aquí si es necesario
            log.error("Error creating order: {}", e.getMessage());
            throw new RuntimeException("Error creating order", e);
        } catch (Exception e) {
            // Manejar otras excepciones aquí si es necesario
            log.error("Error creating order: {}", e.getMessage());
            throw new RuntimeException("Error creating order", e);
        }
    }
*/

}