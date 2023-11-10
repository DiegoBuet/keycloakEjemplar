package com.api.rest.model.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "payment_methods")
@Data
public class PaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Agrega el getter para 'id'
    public Long getId() {
        return id;
    }

    // Agrega el setter para 'id'
    public void setId(Long id) {
        this.id = id;
    }
}
