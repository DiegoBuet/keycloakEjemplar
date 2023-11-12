package com.api.rest.util;

import com.api.rest.model.entities.Address;
import com.api.rest.model.entities.Client;
import com.api.rest.model.entities.PaymentMethod;
import com.api.rest.model.entities.Product;
import com.api.rest.repositories.AddressRepository;
import com.api.rest.repositories.ClientRepository;
import com.api.rest.repositories.PaymentMethodRepository;
import com.api.rest.repositories.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final AddressRepository addressRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final ProductRepository productRepository;

    private final ClientRepository clientRepository;

    public DataInitializer(AddressRepository addressRepository,
                           PaymentMethodRepository paymentMethodRepository,
                           ProductRepository productRepository,
                           ClientRepository clientRepository) {
        this.addressRepository = addressRepository;
        this.paymentMethodRepository = paymentMethodRepository;
        this.productRepository = productRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    public void run(String... args) {
        // Precargar direcciones
        log.info("DataInitializer is init running...");
        List<Address> addresses = Arrays.asList(
                new Address(1L,"123 Main St", "Cityville", "Stateville", "12345"),
                new Address(2L,"456 Oak St", "Townburg", "Stateville", "67890")
                // Agrega más direcciones según sea necesario
        );
        addressRepository.saveAll(addresses);

        // Precargar métodos de pago
        List<PaymentMethod> paymentMethods = Arrays.asList(
                PaymentMethod.builder().cardType("Visa").build(),
                PaymentMethod.builder().cardType("MasterCard").build()
                // Agrega más métodos de pago según sea necesario
        );
        paymentMethodRepository.saveAll(paymentMethods);

        // Precargar productos
        List<Product> products = Arrays.asList(
                new Product(1L, "Laptop", new BigDecimal("999.99"),5),
                new Product(2L, "Smartphone", new BigDecimal("599.99"),11),
                new Product(3L,"Headphones", new BigDecimal("99.99"),9),
                new Product(4L,"Tablet", new BigDecimal("299.99"),2),
                new Product(5L,"Smartwatch", new BigDecimal("199.99"), 3),
                new Product(6L,"Camera", new BigDecimal("499.99"), 8),
                new Product(7L,"Speaker", new BigDecimal("79.99"),7),
                new Product(8L,"Gaming Console", new BigDecimal("399.99"),5),
                new Product(9L,"Fitness Tracker", new BigDecimal("49.99"), 3),
                new Product(10L,"Printer", new BigDecimal("149.99"),4)
        );
        productRepository.saveAll(products);

        // Precargar clientes
        List<Client> clients = Arrays.asList(
                new Client(1L, "john_doe", "john.doe@example.com"),
                new Client(2L, "jane_smith", "jane.smith@example.com")
                // Agrega más clientes según sea necesario
        );
        clientRepository.saveAll(clients);
        log.info("DataInitializer is charged...");
    }


}
