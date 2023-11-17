package com.api.rest.model.entities;


public enum OrderStatus {
    IN_PROGRESS,
    CANCELLED,
    APPROVED,
    COMPLETED,
    PENDING_PAYMENT,  // Nuevo estado para la espera de pago
    PAID,             // Nuevo estado para el pago completado
    IN_TRANSIT,       // Nuevo estado para el envío en tránsito
    DELIVERED,        // Nuevo estado para la entrega completada
    FAILED
}
