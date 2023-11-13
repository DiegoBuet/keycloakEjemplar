    -- src/main/resources/db/migration/h2/V1__init_h2.sql
    CREATE TABLE clients (
        id INT AUTO_INCREMENT PRIMARY KEY,
        username VARCHAR(255),
        email VARCHAR(255)
    );

    INSERT INTO clients (id, username, email) VALUES
    (1, 'john_doe', 'john.doe@example.com'),
    (2, 'jane_smith', 'jane.smith@example.com');