CREATE TABLE addresses (
    id INT AUTO_INCREMENT PRIMARY KEY,
    street VARCHAR(255),
    city VARCHAR(255),
    state VARCHAR(255),
    zip_code VARCHAR(255)
);

INSERT INTO addresses (id, street, city, state, zip_code) VALUES
(1, '123 Main St', 'Cityville', 'Stateville', '12345'),
(2, '456 Oak St', 'Townburg', 'Stateville', '67890');