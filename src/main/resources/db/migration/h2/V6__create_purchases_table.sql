-- src/main/resources/db/migration/h2/V2__create_purchases_table.sql
CREATE TABLE purchases (
    id INT AUTO_INCREMENT PRIMARY KEY,
    client_id INT,
    delivery_address_id INT,
    payment_method_id INT,
    purchase_date TIMESTAMP,
    status VARCHAR(255),
    total_amount DECIMAL(10, 2),
    FOREIGN KEY (client_id) REFERENCES clients(id),
    FOREIGN KEY (delivery_address_id) REFERENCES addresses(id),
    FOREIGN KEY (payment_method_id) REFERENCES payment_methods(id)
);

-- Puedes agregar más detalles según sea necesario
