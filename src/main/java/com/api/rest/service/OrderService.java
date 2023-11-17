package com.api.rest.service;

import com.api.rest.model.dto.DetailedPurchaseDTO;
import com.api.rest.model.dto.OrderRequestDTO;
import com.api.rest.model.entities.Address;
import com.api.rest.model.entities.Order;
import com.api.rest.model.entities.Purchase;
import com.api.rest.model.dto.PaymentDTO;


public interface OrderService {
    DetailedPurchaseDTO createOrder(OrderRequestDTO orderRequestDTO);



}


