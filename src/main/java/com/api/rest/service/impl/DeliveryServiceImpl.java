package com.api.rest.service.impl;

import com.api.rest.model.entities.Address;
import com.api.rest.model.entities.Delivery;
import com.api.rest.model.entities.Order;
import com.api.rest.model.entities.OrderStatus;
import com.api.rest.repositories.DeliveryRepository;
import com.api.rest.service.DeliveryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;

    @Autowired
    public DeliveryServiceImpl(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    @Override
    public Delivery createDelivery(Order order, Address deliveryAddress) {
        try {
            Delivery delivery = new Delivery();
            delivery.setOrder(order);
            delivery.setDeliveryAddress(deliveryAddress);
            delivery.setDeliveryDate(LocalDateTime.now());
            delivery.setStatus(OrderStatus.IN_TRANSIT); // Estado inicial, puedes ajustarlo según tus necesidades

            // Guarda la entrega utilizando el repositorio JPA
            deliveryRepository.save(delivery);

            return delivery;
        } catch (Exception e) {
            log.error("Error creating delivery: {}", e.getMessage());
            throw new RuntimeException("Error creating delivery", e);
        }
    }


}