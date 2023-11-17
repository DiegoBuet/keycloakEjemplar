package com.api.rest.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "deliveries")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "delivery_address_id", nullable = false)
    private Address deliveryAddress;

    @Column(name = "delivery_date")
    private LocalDateTime deliveryDate;


    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;


}