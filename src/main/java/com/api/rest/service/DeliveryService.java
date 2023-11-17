package com.api.rest.service;

import com.api.rest.model.entities.Address;
import com.api.rest.model.entities.Delivery;
import com.api.rest.model.entities.Order;

public interface DeliveryService {
    Delivery createDelivery(Order order, Address deliveryAddress);
}
