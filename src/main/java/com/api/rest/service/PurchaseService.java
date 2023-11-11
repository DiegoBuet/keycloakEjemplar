package com.api.rest.service;

import com.api.rest.model.dto.DetailedPurchaseDTO;
import com.api.rest.model.dto.ProductDTO;
import com.api.rest.model.dto.PurchaseDTO;
import com.api.rest.model.entities.*;
import com.api.rest.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;
    private final AddressRepository addressRepository;
    private final PaymentMethodRepository paymentMethodRepository;

    @Autowired
    public PurchaseService(PurchaseRepository purchaseRepository, ClientRepository clientRepository,
                           ProductRepository productRepository, AddressRepository addressRepository,
                           PaymentMethodRepository paymentMethodRepository) {
        this.purchaseRepository = purchaseRepository;
        this.clientRepository = clientRepository;
        this.productRepository = productRepository;
        this.addressRepository = addressRepository;
        this.paymentMethodRepository = paymentMethodRepository;
    }


    public DetailedPurchaseDTO changeAddress(Long idPurchase, Long idAddress) {
        try {
            Purchase purchase = getPurchaseById(idPurchase);
            Address newAddress = getAddressById(idAddress);

            purchase.setDeliveryAddress(newAddress);

            Purchase savedPurchase = purchaseRepository.save(purchase);

            return mapToDetailedPurchaseDTO(savedPurchase);
        } catch (EntityNotFoundException e) {
            log.error("Error while changing address: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error while changing address: {}", e.getMessage());
            throw new RuntimeException("Error changing address", e);
        }
    }

    public DetailedPurchaseDTO changePaymentMethod(Long idPurchase, Long idPaymentMethod) {
        try {
            Purchase purchase = getPurchaseById(idPurchase);
            PaymentMethod newPaymentMethod = getPaymentMethodById(idPaymentMethod);

            purchase.setPaymentMethod(newPaymentMethod);

            Purchase savedPurchase = purchaseRepository.save(purchase);

            return mapToDetailedPurchaseDTO(savedPurchase);
        } catch (EntityNotFoundException e) {
            log.error("Error while changing payment method: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error while changing payment method: {}", e.getMessage());
            throw new RuntimeException("Error changing payment method", e);
        }
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
        return paymentMethodRepository.findById(paymentMethodId)
                .orElseThrow(() -> new EntityNotFoundException("Método de pago no encontrado"));
    }

    private DetailedPurchaseDTO mapToDetailedPurchaseDTO(Purchase purchase) {
        List<ProductDTO> productDTOs = purchase.getPurchaseItems().stream()
                .map((PurchaseItem purchaseItem) -> mapToProductDTO(purchaseItem))
                .collect(Collectors.toList());

        return DetailedPurchaseDTO.builder()
                .id(purchase.getId())
                .clientId(purchase.getClient().getId())
                .products(productDTOs)
                .addressId(purchase.getDeliveryAddress().getId())
                .paymentMethodId(purchase.getPaymentMethod().getId())
                .status(purchase.getStatus())  // Asegúrate de tener el campo 'status' en la entidad 'Purchase'
                .build();
    }

    private ProductDTO mapToProductDTO(PurchaseItem purchaseItem) {
        Product product = purchaseItem.getProduct();
        return ProductDTO.builder()
                .productId(product.getId())
                .quantity(purchaseItem.getQuantity())
                .build();
    }

    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(this::mapToProductDTO)
                .collect(Collectors.toList());
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

    public List<ProductDTO> getCurrentPurchase(Long clientId) {
        try {
            Client client = getClientById(clientId);
            Optional<Purchase> currentPurchase = purchaseRepository.findFirstByClientAndStatus(client, PurchaseStatus.IN_PROGRESS);

            if (currentPurchase.isPresent()) {
                return currentPurchase.get().getPurchaseItems().stream()
                        .map(this::mapToProductDTO)
                        .collect(Collectors.toList());
            } else {
                throw new EntityNotFoundException("Compra actual no encontrada para el cliente");
            }
        } catch (EntityNotFoundException e) {
            // Manejar la excepción aquí o relanzarla según sea necesario
            throw new RuntimeException("Error al obtener la compra actual", e);
        }
    }


    private void updatePurchaseTotalAmount(Purchase purchase) {
        List<PurchaseItem> purchaseItems = purchase.getPurchaseItems();

        // Utilizar Java Streams y reducción para calcular el totalAmount
        BigDecimal totalAmount = purchaseItems.stream()
                .map(item -> BigDecimal.valueOf(item.getProduct().getPrice())
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        purchase.setTotalAmount(totalAmount);
    }

    public DetailedPurchaseDTO startPurchase(PurchaseDTO purchaseDTO) {
        try {
            Client client = getClientById(purchaseDTO.getClientId());
            Address address = getAddressById(purchaseDTO.getAddressId());
            PaymentMethod paymentMethod = getPaymentMethodById(purchaseDTO.getPaymentMethodId());

            Purchase newPurchase = new Purchase();
            // Log para verificar si los datos precargados están funcionando
            log.info("Clientes precargados: {}", clientRepository.findAll());
            log.info("Direcciones precargadas: {}", addressRepository.findAll());
            log.info("Métodos de pago precargados: {}", paymentMethodRepository.findAll());
            log.info("Productos precargados: {}", productRepository.findAll());
            newPurchase.setClient(client);
            newPurchase.setDeliveryAddress(address);
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
            newPurchase.setStatus(PurchaseStatus.IN_PROGRESS); // Asegúrate de establecer el estado

            Purchase savedPurchase = purchaseRepository.save(newPurchase);

            return mapToDetailedPurchaseDTO(savedPurchase);
        } catch (EntityNotFoundException e) {
            log.error("Error while starting purchase: {}", e.getMessage());
            throw e;  // Re-lanzar la excepción para que se maneje en el controlador
        } catch (Exception e) {
            log.error("Error while starting purchase: {}", e.getMessage());
            throw new RuntimeException("Error starting purchase", e);
        }
    }

  /*  public DetailedPurchaseDTO addProduct(Long idPurchase, ProductDTO productDTO) {
        try {
            Purchase purchase = getPurchaseById(idPurchase);
            Product product = getProductById(productDTO.getProductId());

            PurchaseItem purchaseItem = new PurchaseItem();
            purchaseItem.setProduct(product);
            purchaseItem.setQuantity(productDTO.getQuantity());
            purchaseItem.setPurchase(purchase);

            purchase.getPurchaseItems().add(purchaseItem);
            updatePurchaseTotalAmount(purchase);

            Purchase savedPurchase = purchaseRepository.save(purchase);

            return mapToDetailedPurchaseDTO(savedPurchase);
        } catch (EntityNotFoundException e) {
            log.error("Error while adding product: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error while adding product: {}", e.getMessage());
            throw new RuntimeException("Error adding product", e);
        }
    }*/

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

            updatePurchaseTotalAmount(purchase);

            Purchase savedPurchase = purchaseRepository.save(purchase);

            return mapToDetailedPurchaseDTO(savedPurchase);
        } catch (EntityNotFoundException e) {
            log.error("Error while modifying product: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error while modifying product: {}", e.getMessage());
            throw new RuntimeException("Error modifying product", e);
        }
    }

    public void removeProduct(Long idPurchase, Long idProduct) {
        try {
            Purchase purchase = getPurchaseById(idPurchase);
            Product product = getProductById(idProduct);

            // Encuentra y elimina el PurchaseItem correspondiente al producto
            purchase.getPurchaseItems().removeIf(purchaseItem -> purchaseItem.getProduct().equals(product));

            // Actualiza el monto total de la compra
            updatePurchaseTotalAmount(purchase);

            // Guarda la compra actualizada
            purchaseRepository.save(purchase);
        } catch (EntityNotFoundException e) {
            log.error("Error while removing product: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error while removing product: {}", e.getMessage());
            throw new RuntimeException("Error removing product", e);
        }
    }


  /*  public DetailedPurchaseDTO addProduct(Long idPurchase, ProductDTO productDTO) {
        Purchase purchase = getPurchaseById(idPurchase);

        // Intenta obtener el producto por ID
        Product product = getProductById(productDTO.getProductId());

        if (product == null) {
            // Si el producto no existe, crea uno nuevo
            product = new Product();
            // Configura los detalles del nuevo producto (puedes ajustar según tus necesidades)
            product.setId(productDTO.getProductId());
            product.setName("Nuevo Producto");
            product.setPrice(0.0);  // Puedes establecer el precio predeterminado o ajustarlo según sea necesario
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
    }
*/
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
              product.setPrice(0.0);  // Puedes establecer el precio predeterminado o ajustarlo según sea necesario
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


}

