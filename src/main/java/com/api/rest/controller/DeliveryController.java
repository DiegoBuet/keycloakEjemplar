package com.api.rest.controller;


import com.api.rest.model.entities.Delivery;
import com.api.rest.model.entities.Order;
import com.api.rest.model.entities.Address;
import com.api.rest.service.DeliveryService;
import com.api.rest.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/deliveries")
@Slf4j
public class DeliveryController {

    private final DeliveryService deliveryService;
    private final OrderService orderService;

    @Autowired
    public DeliveryController(DeliveryService deliveryService, OrderService orderService) {
        this.deliveryService = deliveryService;
        this.orderService = orderService;
    }

/*    @PostMapping("/create")
    public ResponseEntity<Delivery> createDelivery(@RequestParam Long orderId, @RequestParam Long deliveryAddressId) {
        try {
            Order order = orderService.getOrderById(orderId);
            Address deliveryAddress = orderService.getAddressById(deliveryAddressId);

            Delivery createdDelivery = deliveryService.createDelivery(order, deliveryAddress);

            return new ResponseEntity<>(createdDelivery, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            log.error("Error creating delivery: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Error creating delivery: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }*/

    // Otros endpoints relacionados con las entregas (actualización de estado, obtener detalles, etc.)
}