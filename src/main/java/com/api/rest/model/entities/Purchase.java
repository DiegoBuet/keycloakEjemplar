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

        @Getter
        @ManyToOne
        @JoinColumn(name = "client_id", nullable = false)
        private Client client;

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

        @Column(name = "status")
        @Enumerated(EnumType.STRING)
        private OrderStatus status;

        @Column(name = "payment_amount") // Cambié el nombre aquí para evitar la colisión
        private BigDecimal paymentAmount;

        @Column(name = "payment_status")
        @Enumerated(EnumType.STRING)
        private OrderStatus paymentStatus;


        public void setClient(Client client) {
            this.client = client;
        }

        public void setStatus(OrderStatus status) {
            this.status = status;
        }


        public void setPaymentAmount(BigDecimal paymentAmount) {
            this.paymentAmount = paymentAmount;
        }
    }
