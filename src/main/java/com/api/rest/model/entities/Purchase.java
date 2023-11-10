    package com.api.rest.model.entities;


    import jakarta.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.Getter;
    import lombok.NoArgsConstructor;

    import java.math.BigDecimal;
    import java.time.LocalDateTime;
    import java.util.List;

    @Entity
    @Table(name = "purchases")
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class Purchase {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        // Agrega el getter para 'client'
        @Getter
        @ManyToOne
        @JoinColumn(name = "client_id", nullable = false)  // Cambia a "client_id"
        private Client client;  // Cambia a "Client"

        @OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<PurchaseItem> purchaseItems;

        @ManyToOne
        @JoinColumn(name = "delivery_address_id")
        private Address deliveryAddress;

        @ManyToOne
        @JoinColumn(name = "payment_method_id")
        private PaymentMethod paymentMethod;

        @Column(name = "total_amount")
        private BigDecimal totalAmount;

        @Column(name = "purchase_date")
        private LocalDateTime purchaseDate;

        // Agrega el getter para 'status'

        @Getter
        @ManyToOne
        @JoinColumn(name = "status_id") // Ajusta el nombre de la columna según tu modelo
        private PurchaseStatus status;


        // Agrega el setter para 'client'
        public void setClient(Client client) {
            this.client = client;
        }

        // Agrega el setter para 'status'
        public void setStatus(PurchaseStatus status) {
            this.status = status;
        }
    }
