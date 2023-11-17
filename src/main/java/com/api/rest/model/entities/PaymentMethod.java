package com.api.rest.model.entities;

import jakarta.persistence.*;
import lombok.*;

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

    @Getter@Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method_type")
    private PaymentMethodType paymentMethodType;;


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public PaymentMethodType getType() {
        return paymentMethodType;
    }

    public void setType(PaymentMethodType paymentMethodType) {
        this.paymentMethodType = paymentMethodType;
    }

}
