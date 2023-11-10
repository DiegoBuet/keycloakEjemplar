package com.api.rest.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "purchase_status")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "status_name", nullable = false, unique = true)
    private String statusName;

    // Agrega el getter para 'id'
    public Long getId() {
        return id;
    }

    // Agrega el setter para 'id'
    public void setId(Long id) {
        this.id = id;
    }
}
