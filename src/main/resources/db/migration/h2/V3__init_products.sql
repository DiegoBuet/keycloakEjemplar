-- src/main/resources/db/migration/h2/V1__init_h2.sql
CREATE TABLE products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    price DECIMAL(10, 2),
    quantity INT
);

INSERT INTO products (id, name, price, quantity) VALUES
(1, 'Laptop', 999.99, 5),
(2, 'Smartphone', 599.99, 11),
(3, 'Headphones', 99.99, 9),
(4, 'Tablet', 299.99, 2),
(5, 'Smartwatch', 199.99, 3),
(6, 'Camera', 499.99, 8),
(7, 'Speaker', 79.99, 7),
(8, 'Gaming Console', 399.99, 5),
(9, 'Fitness Tracker', 49.99, 3),
(10, 'Printer', 149.99, 5)
