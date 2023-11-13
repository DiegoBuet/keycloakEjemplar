-- Crear la tabla payment_methods
CREATE TABLE payment_methods (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cardtype VARCHAR(255)
);

-- Insertar datos
INSERT INTO payment_methods (id, cardtype) VALUES
(1, 'Visa'),
(2, 'MasterCard');