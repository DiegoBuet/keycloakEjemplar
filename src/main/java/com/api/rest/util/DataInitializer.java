package com.api.rest.util;

import com.api.rest.model.entities.Address;
import com.api.rest.model.entities.PaymentMethod;
import com.api.rest.model.entities.Product;
import com.api.rest.repositories.AddressRepository;
import com.api.rest.repositories.PaymentMethodRepository;
import com.api.rest.repositories.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final AddressRepository addressRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final ProductRepository productRepository;

    public DataInitializer(AddressRepository addressRepository,
                           PaymentMethodRepository paymentMethodRepository,
                           ProductRepository productRepository) {
        this.addressRepository = addressRepository;
        this.paymentMethodRepository = paymentMethodRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) {
        // Precargar direcciones
        List<Address> addresses = Arrays.asList(
                new Address(1L,"123 Main St", "Cityville", "Stateville", "12345"),
                new Address(2L,"456 Oak St", "Townburg", "Stateville", "67890")
                // Agrega más direcciones según sea necesario
        );
        addressRepository.saveAll(addresses);

        // Precargar métodos de pago
        List<PaymentMethod> paymentMethods = Arrays.asList(
                new PaymentMethod(),
                new PaymentMethod()
                // Agrega más métodos de pago según sea necesario
        );
        paymentMethodRepository.saveAll(paymentMethods);

        // Precargar productos
        List<Product> products = Arrays.asList(
                new Product(1L, "Laptop", 999.99),
                new Product(2L, "Smartphone", 599.99),
                new Product(3L,"Headphones", 99.99),
                new Product(4L,"Tablet", 299.99),
                new Product(5L,"Smartwatch", 199.99),
                new Product(6L,"Camera", 499.99),
                new Product(7L,"Speaker", 79.99),
                new Product(8L,"Gaming Console", 399.99),
                new Product(9L,"Fitness Tracker", 49.99),
                new Product(10L,"Printer", 149.99)
        );
        productRepository.saveAll(products);
    }
}
