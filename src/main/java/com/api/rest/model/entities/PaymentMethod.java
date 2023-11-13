package com.api.rest.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payment_methods")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cardtype")
    private String cardtype;

    // Agrega el getter para 'id'
    public Long getId() {
        return id;
    }

    // Agrega el setter para 'id'
    public void setId(Long id) {
        this.id = id;
    }

    // Agrega el getter y setter para 'cardType'
    public String getCardType() {
        return cardtype;
    }

    public void setCardType(String cardtype) {
        this.cardtype = cardtype;
    }
}
