-- Crear la tabla payment_methods
CREATE TABLE payment_methods (
    id INT AUTO_INCREMENT PRIMARY KEY,
    payment_method_type VARCHAR(255)
);

-- Insertar datos
INSERT INTO payment_methods (id, payment_method_type) VALUES
(1, 'CASH'),
(2, 'CREDIT_CARD'),
(3, 'DEBIT_CARD'),
(4, 'BANK_TRANSFER');