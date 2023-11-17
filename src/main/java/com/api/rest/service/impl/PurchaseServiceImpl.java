package com.api.rest.service.impl;


import com.api.rest.model.dto.*;
import com.api.rest.model.entities.*;
import com.api.rest.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PurchaseServiceImpl {

    private final PurchaseRepository purchaseRepository;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;
    private final AddressRepository addressRepository;
    private final PaymentMethodRepository paymentMethodRepository;


    public PurchaseServiceImpl(PurchaseRepository purchaseRepository, ClientRepository clientRepository, ProductRepository productRepository, AddressRepository addressRepository, PaymentMethodRepository paymentMethodRepository) {
        this.purchaseRepository = purchaseRepository;
        this.clientRepository = clientRepository;
        this.productRepository = productRepository;
        this.addressRepository = addressRepository;
        this.paymentMethodRepository = paymentMethodRepository;
    }

    private Product getProductById(Long idProduct) {
        return productRepository.findById(idProduct)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));
    }

    private Purchase getPurchaseById(Long idPurchase) {
        return purchaseRepository.findById(idPurchase)
                .orElseThrow(() -> new EntityNotFoundException("Compra no encontrada"));
    }

    private Address getAddressById(Long addressId) {
        return addressRepository.findById(addressId)
                .orElseThrow(() -> new EntityNotFoundException("Dirección no encontrada"));
    }

    private PaymentMethod getPaymentMethodById(Long paymentMethodId) {
        if (paymentMethodId == null) {
            // Si el paymentMethodId es nulo, asignar el PaymentMethod con ID 1 (por ejemplo, Visa)
            return paymentMethodRepository.findById(1L)
                    .orElseThrow(() -> new EntityNotFoundException("Método de pago predeterminado no encontrado"));
        } else {
            // Si el paymentMethodId no es nulo, buscar el PaymentMethod por ID
            return paymentMethodRepository.findById(paymentMethodId)
                    .orElseThrow(() -> new EntityNotFoundException("Método de pago no encontrado"));
        }
    }
    private ProductDTO mapToProductDTO(Product product) {
        return ProductDTO.builder()
                .productId(product.getId())
                .quantity(0)  // Puedes ajustar según sea necesario
                .build();
    }

    private DetailedPurchaseDTO mapToDetailedPurchaseDTO(Purchase purchase, List<ProductDTO> productDTOs) {
        return DetailedPurchaseDTO.builder()
                .id(purchase.getId())
                .clientId(purchase.getClient().getId())
                .products(productDTOs)
                .addressId(purchase.getDeliveryAddress().getId())
                .paymentMethodId(purchase.getPaymentMethod().getId())
                .status(purchase.getStatus())
                .build();
    }

    public DetailedPurchaseDTO getPurchase(Long idPurchase) {
        Purchase purchase = getPurchaseById(idPurchase);
        List<ProductDTO> productDTOs = getAllProducts();  // Obtén la lista de productos
        return mapToDetailedPurchaseDTO(purchase, productDTOs);
    }
    private Client getClientById(Long clientId) {
        Optional<Client> optionalClient = clientRepository.findById(clientId);
        if (optionalClient.isPresent()) {
            return optionalClient.get();
        } else {
            log.error("Cliente no encontrado con ID: {}", clientId);
            throw new EntityNotFoundException("Cliente no encontrado");
        }
    }

    @Transactional
    public DetailedPurchaseDTO removeProduct(Long idPurchase, Long idProduct) {
        try {
            Purchase purchase = getPurchaseById(idPurchase);

            // Encuentra y elimina el PurchaseItem correspondiente al producto
            purchase.getPurchaseItems().removeIf(purchaseItem -> purchaseItem.getProduct().getId().equals(idProduct));

            // Actualiza el monto total de la compra
            updatePurchaseTotalAmount(purchase);

            // Guarda la compra actualizada
            Purchase savedPurchase = purchaseRepository.save(purchase);

            // Devuelve el estado actualizado de la compra
            return mapToDetailedPurchaseDTO(savedPurchase);
        } catch (EntityNotFoundException e) {
            log.error("Error while removing product: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error while removing product: {}", e.getMessage());
            throw new ServiceException("Error removing product", e);
        }
    }
    private ProductResponseDTO mapToProductResponseDTO(PurchaseItem purchaseItem) {
        Product product = purchaseItem.getProduct();
        return ProductResponseDTO.builder()
                .productId(product.getId())
                .productName(product.getName())
                .productPrice(product.getPrice())
                .quantity(purchaseItem.getQuantity())
                .build();
    }

    private ProductDTO mapToProductDTO(PurchaseItem purchaseItem, BigDecimal totalAmount) {
        Product product = purchaseItem.getProduct();
        return ProductDTO.builder()
                .productId(product.getId())
                .quantity(purchaseItem.getQuantity())
                .productName(product.getName())
                .productPrice(product.getPrice())
                // .totalAmount(totalAmount != null ? totalAmount.doubleValue() : 0.0)  // Verifica si totalAmount es null
                .build();
    }
    private DetailedPurchaseDTO mapToDetailedPurchaseDTO(Purchase purchase) {
        List<ProductDTO> productDTOs = purchase.getPurchaseItems().stream()
                .map((PurchaseItem purchaseItem) -> mapToProductDTO(purchaseItem, purchase.getTotalAmount()))
                .collect(Collectors.toList());

        return DetailedPurchaseDTO.builder()
                .id(purchase.getId())
                .clientId(purchase.getClient().getId())
                .products(productDTOs)
                .addressId(purchase.getDeliveryAddress().getId())
                .paymentMethodId(purchase.getPaymentMethod().getId())
                .status(purchase.getStatus())
                .build();
    }

    private BigDecimal getTotalAmountWithDefault(Purchase purchase) {
        BigDecimal totalAmount = purchase.getTotalAmount();
        return (totalAmount != null) ? totalAmount : BigDecimal.ZERO;
    }

    @Transactional
    public DetailedPurchaseDTO addProduct(Long idPurchase, ProductDTO productDTO) {
        try {
            Purchase purchase = getPurchaseById(idPurchase);

            // Intenta obtener el producto por ID
            Product product = getProductById(productDTO.getProductId());

            if (product == null) {
                // Si el producto no existe, crea uno nuevo
                product = new Product();
                // Configura los detalles del nuevo producto (puedes ajustar según tus necesidades)
                product.setId(productDTO.getProductId());
                product.setName("Nuevo Producto");
                product.setPrice(BigDecimal.valueOf(0.0)); // Puedes establecer el precio predeterminado o ajustarlo según sea necesario
            }

            // Crea un nuevo PurchaseItem
            PurchaseItem purchaseItem = new PurchaseItem();
            purchaseItem.setProduct(product);
            purchaseItem.setQuantity(productDTO.getQuantity());
            purchaseItem.setPurchase(purchase);

            // Agrega el PurchaseItem a la compra
            purchase.getPurchaseItems().add(purchaseItem);

            // Actualiza el monto total de la compra
            updatePurchaseTotalAmount(purchase);

            // Guarda la compra actualizada
            Purchase savedPurchase = purchaseRepository.save(purchase);

            return mapToDetailedPurchaseDTO(savedPurchase);
        } catch (EntityNotFoundException e) {
            log.error("Error adding product: {}", e.getMessage());
            throw e;  // Re-lanzar la excepción para que se maneje en el controlador
        } catch (Exception e) {
            log.error("Error adding product: {}", e.getMessage());
            throw new RuntimeException("Error adding product", e);
        }
    }
    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(this::mapToProductDTOWithNameAndPrice)
                .collect(Collectors.toList());
    }

    private ProductDTO mapToProductDTOWithNameAndPrice(Product product) {
        return ProductDTO.builder()
                .productId(product.getId())
                .productName(product.getName())
                .productPrice(product.getPrice())
                .quantity(product.getQuantity())  // Agrega la cantidad
                .build();
    }

    private ProductDTO mapToProductDTO(PurchaseItem purchaseItem) {
        Product product = purchaseItem.getProduct();
        return ProductDTO.builder()
                .productId(product.getId())
                .quantity(purchaseItem.getQuantity())
                .build();
    }

    private void updatePurchaseTotalAmount(Purchase purchase) {
        List<PurchaseItem> purchaseItems = purchase.getPurchaseItems();

        // Utiliza Java Streams y reducción para calcular el totalAmount
        BigDecimal totalAmount = purchaseItems.stream()
                .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        purchase.setTotalAmount(totalAmount);
    }


    public DetailedPurchaseDTO modifyProduct(Long idPurchase, ProductDTO productDTO) {
        try {
            Purchase purchase = getPurchaseById(idPurchase);
            Product product = getProductById(productDTO.getProductId());

            // Encuentra el PurchaseItem correspondiente al producto
            PurchaseItem purchaseItem = purchase.getPurchaseItems().stream()
                    .filter(item -> item.getProduct().equals(product))
                    .findFirst()
                    .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado en la compra"));

            // Modifica la cantidad del producto en el PurchaseItem
            purchaseItem.setQuantity(productDTO.getQuantity());

            // Utiliza la lógica del método addProduct para actualizar el producto
            return addProductLogic(purchase, productDTO);
        } catch (EntityNotFoundException e) {
            log.error("Error while modifying product: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error while modifying product: {}", e.getMessage());
            throw new RuntimeException("Error modifying product", e);
        }
    }

    private DetailedPurchaseDTO addProductLogic(Purchase purchase, ProductDTO productDTO) {
        // Intenta obtener el producto por ID
        Product product = getProductById(productDTO.getProductId());

        if (product == null) {
            // Si el producto no existe, crea uno nuevo
            product = new Product();
            // Configura los detalles del nuevo producto (puedes ajustar según tus necesidades)
            product.setId(productDTO.getProductId());
            product.setName("Nuevo Producto");
            product.setPrice(BigDecimal.valueOf(0.0)); // Puedes establecer el precio predeterminado o ajustarlo según sea necesario
        }

        PurchaseItem purchaseItem = new PurchaseItem();
        purchaseItem.setProduct(product);
        purchaseItem.setQuantity(productDTO.getQuantity());
        purchaseItem.setPurchase(purchase);

        purchase.getPurchaseItems().add(purchaseItem);

        updatePurchaseTotalAmount(purchase);

        Purchase savedPurchase = purchaseRepository.save(purchase);

        return mapToDetailedPurchaseDTO(savedPurchase);
    }

    public PurchaseResponseDTO getCurrentPurchase(Long clientId) {
        try {
            Client client = getClientById(clientId);
            Optional<Purchase> currentPurchase = purchaseRepository.findFirstByClientAndStatus(client, OrderStatus.IN_PROGRESS);

            if (currentPurchase.isPresent()) {
                Purchase purchase = currentPurchase.get();
                updatePurchaseTotalAmount(purchase);

                List<ProductResponseDTO> productDTOs = purchase.getPurchaseItems().stream()
                        .map(this::mapToProductResponseDTO)
                        .collect(Collectors.toList());

                PurchaseResponseDTO purchaseResponseDTO = PurchaseResponseDTO.builder()
                        .purchaseId(purchase.getId())
                        .clientId(clientId)
                        .products(productDTOs)
                        .addressId(purchase.getDeliveryAddress().getId())
                        .paymentMethodId(purchase.getPaymentMethod().getId())
                        .totalAmount(getTotalAmountWithDefault(purchase).doubleValue())
                        .build();

                return purchaseResponseDTO;
            } else {
                throw new EntityNotFoundException("Compra actual no encontrada para el cliente");
            }
        } catch (EntityNotFoundException e) {
            throw new RuntimeException("Error al obtener la compra actual", e);
        }
    }

    public DetailedPurchaseDTO changePaymentMethod(Long idPurchase, Long idPaymentMethod) {
        try {
            Purchase purchase = getPurchaseById(idPurchase);

            // Verificar que la compra esté en un estado adecuado para cambiar el método de pago (por ejemplo, "En Progreso")
            if (purchase.getStatus() != OrderStatus.IN_PROGRESS) {
                throw new IllegalStateException("No se puede cambiar el método de pago de una compra que no está en progreso.");
            }

            PaymentMethod newPaymentMethod = getPaymentMethodById(idPaymentMethod);

            purchase.setPaymentMethod(newPaymentMethod);

            Purchase savedPurchase = purchaseRepository.save(purchase);

            return mapToDetailedPurchaseDTO(savedPurchase);
        } catch (EntityNotFoundException e) {
            log.error("Error while changing payment method: {}", e.getMessage());
            throw e;
        } catch (IllegalStateException e) {
            log.error("Error while changing payment method: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error while changing payment method: {}", e.getMessage());
            throw new RuntimeException("Error changing payment method", e);
        }
    }

    @Transactional
    public DetailedPurchaseDTO completePurchase(Long idPurchase) {
        try {
            Purchase purchase = getPurchaseById(idPurchase);

            if (purchase.getStatus() == OrderStatus.COMPLETED) {
                throw new RuntimeException("La compra ya está completa");
            }

            purchase.setStatus(OrderStatus.COMPLETED);


            Purchase savedPurchase = purchaseRepository.save(purchase);

            return mapToDetailedPurchaseDTO(savedPurchase);
        } catch (EntityNotFoundException e) {
            log.error("Error while completing purchase: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error while completing purchase: {}", e.getMessage());
            throw new RuntimeException("Error completing purchase", e);
        }
    }

    public DetailedPurchaseDTO changeAddress(Long idPurchase, Long idAddress) {
        try {
            Purchase purchase = getPurchaseById(idPurchase);

            // Verificar que la compra esté en un estado adecuado para cambiar la dirección (por ejemplo, "En Progreso")
            if (purchase.getStatus() != OrderStatus.IN_PROGRESS) {
                throw new IllegalStateException("No se puede cambiar la dirección de una compra que no está en progreso.");
            }

            Address existingAddress = getAddressById(idAddress);

            purchase.setDeliveryAddress(existingAddress);

            Purchase savedPurchase = purchaseRepository.save(purchase);

            return mapToDetailedPurchaseDTO(savedPurchase);
        } catch (EntityNotFoundException e) {
            log.error("Error while changing address: {}", e.getMessage());
            throw e;
        } catch (IllegalStateException e) {
            log.error("Error while changing address: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error while changing address: {}", e.getMessage());
            throw new RuntimeException("Error changing address", e);
        }
    }
    private Address mapToAddress(AddressDTO addressDTO) {
        Optional<Address> existingAddress = addressRepository.findByStreetAndCityAndStateAndZipCode(
                addressDTO.getStreet(), addressDTO.getCity(), addressDTO.getState(), addressDTO.getZipCode());

        return existingAddress.orElseGet(() -> {
            Address newAddress = new Address();
            newAddress.setStreet(addressDTO.getStreet());
            newAddress.setCity(addressDTO.getCity());
            newAddress.setState(addressDTO.getState());
            newAddress.setZipCode(addressDTO.getZipCode());
            return newAddress;
        });
    }
    public Address changeAddress(Long idPurchase, AddressDTO newAddressDTO) {
        try {
            Purchase purchase = getPurchaseById(idPurchase);

            // Verificar que la compra esté en un estado adecuado para cambiar la dirección (por ejemplo, "En Progreso")
            if (purchase.getStatus() != OrderStatus.IN_PROGRESS) {
                throw new IllegalStateException("No se puede cambiar la dirección de una compra que no está en progreso.");
            }

            Address newAddress = mapToAddress(newAddressDTO);

            // Guardar explícitamente la dirección antes de asignarla a la compra
            Address savedAddress = addressRepository.save(newAddress);

            purchase.setDeliveryAddress(savedAddress);

            Purchase savedPurchase = purchaseRepository.save(purchase);

            return savedPurchase.getDeliveryAddress();
        } catch (EntityNotFoundException e) {
            log.error("Error while changing address: {}", e.getMessage());
            throw e;
        } catch (IllegalStateException e) {
            log.error("Error while changing address: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error while changing address: {}", e.getMessage());
            throw new RuntimeException("Error changing address", e);
        }
    }

    public void printSavedAddress(Long addressId) {
        try {
            // Obtén la dirección por ID
            Address savedAddress = addressRepository.findById(addressId)
                    .orElseThrow(() -> new EntityNotFoundException("Dirección no encontrada"));

            // Imprime la dirección
            log.info("Dirección guardada: {}", savedAddress);
        } catch (EntityNotFoundException e) {
            log.error("Error al imprimir la dirección guardada: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error al imprimir la dirección guardada: {}", e.getMessage());
            throw new RuntimeException("Error al imprimir la dirección guardada", e);
        }
    }

    public DetailedPurchaseDTO startPurchase(PurchaseDTO purchaseDTO) {
        try {
            Client client = getClientById(purchaseDTO.getClientId());
            Address existingAddress = getAddressById(purchaseDTO.getAddressId());

            // Imprime la dirección antes de guardarla
            log.debug("Address before saving: {}", existingAddress);

            PaymentMethod paymentMethod = getPaymentMethodById(purchaseDTO.getPaymentMethodId());

            Purchase newPurchase = new Purchase();

            newPurchase.setClient(client);
            newPurchase.setDeliveryAddress(existingAddress);
            newPurchase.setPaymentMethod(paymentMethod);

            // Mapeo de ProductDTO a PurchaseItem
            List<PurchaseItem> purchaseItems = purchaseDTO.getProducts().stream()
                    .map(productDTO -> {
                        Product product = getProductById(productDTO.getProductId());
                        PurchaseItem purchaseItem = new PurchaseItem();
                        purchaseItem.setProduct(product);
                        purchaseItem.setQuantity(productDTO.getQuantity());
                        purchaseItem.setPurchase(newPurchase);
                        return purchaseItem;
                    })
                    .collect(Collectors.toList());

            newPurchase.setPurchaseItems(purchaseItems);
            newPurchase.setStatus(OrderStatus.IN_PROGRESS); // Asegúrate de establecer el estado

            Purchase savedPurchase = purchaseRepository.save(newPurchase);

            // Imprime la dirección después de guardarla
            log.debug("Address after saving: {}", savedPurchase.getDeliveryAddress());

            // Calcula el total amount después de guardar la compra
            updatePurchaseTotalAmount(savedPurchase);

            // Retorna el DTO con la información necesaria sin el total amount
            return mapToDetailedPurchaseDTO(savedPurchase);
        } catch (EntityNotFoundException e) {
            log.error("Error while starting purchase: {}", e.getMessage());
            throw e;  // Re-lanzar la excepción para que se maneje en el controlador
        } catch (Exception e) {
            log.error("Error while starting purchase: {}", e.getMessage());
            throw new RuntimeException("Error starting purchase", e);
        }
    }
}