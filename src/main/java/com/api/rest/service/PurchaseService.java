package com.api.rest.service;

import com.api.rest.model.dto.DetailedPurchaseDTO;
import com.api.rest.model.dto.ProductDTO;
import com.api.rest.model.dto.PurchaseDTO;
import com.api.rest.model.entities.*;
import com.api.rest.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
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

    public DetailedPurchaseDTO startPurchase(PurchaseDTO purchaseDTO) {
        Client client = getClientById(purchaseDTO.getClientId());
        Address address = getAddressById(purchaseDTO.getAddressId());
        PaymentMethod paymentMethod = getPaymentMethodById(purchaseDTO.getPaymentMethodId());

        Purchase newPurchase = new Purchase();
        newPurchase.setClient(client);
        newPurchase.setDeliveryAddress(address);
        newPurchase.setPaymentMethod(paymentMethod);

        Purchase savedPurchase = purchaseRepository.save(newPurchase);

        return mapToDetailedPurchaseDTO(savedPurchase);
    }

    public DetailedPurchaseDTO addProduct(Long idPurchase, ProductDTO productDTO) {
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
    }

    public DetailedPurchaseDTO modifyProduct(Long idPurchase, ProductDTO productDTO) {
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
    }

    public void removeProduct(Long idPurchase, Long idProduct) {
        // Implement logic to remove a product from the purchase
    }

    public DetailedPurchaseDTO changeAddress(Long idPurchase, Long idAddress) {
        Purchase purchase = getPurchaseById(idPurchase);
        Address newAddress = getAddressById(idAddress);

        purchase.setDeliveryAddress(newAddress);

        Purchase savedPurchase = purchaseRepository.save(purchase);

        return mapToDetailedPurchaseDTO(savedPurchase);
    }

    public DetailedPurchaseDTO changePaymentMethod(Long idPurchase, Long idPaymentMethod) {
        Purchase purchase = getPurchaseById(idPurchase);
        PaymentMethod newPaymentMethod = getPaymentMethodById(idPaymentMethod);

        purchase.setPaymentMethod(newPaymentMethod);

        Purchase savedPurchase = purchaseRepository.save(purchase);

        return mapToDetailedPurchaseDTO(savedPurchase);
    }

    public DetailedPurchaseDTO getPurchase(Long idPurchase) {
        Purchase purchase = getPurchaseById(idPurchase);
        return mapToDetailedPurchaseDTO(purchase);
    }

    private Product getProductById(Long idProduct) {
        return productRepository.findById(idProduct)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));
    }

    private Purchase getPurchaseById(Long idPurchase) {
        return purchaseRepository.findById(idPurchase)
                .orElseThrow(() -> new EntityNotFoundException("Compra no encontrada"));
    }

    private Client getClientById(Long clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado"));
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

    private void updatePurchaseTotalAmount(Purchase purchase) {
        List<PurchaseItem> purchaseItems = purchase.getPurchaseItems();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (PurchaseItem purchaseItem : purchaseItems) {
            Product product = purchaseItem.getProduct();
            int quantity = purchaseItem.getQuantity();
            BigDecimal productPrice = BigDecimal.valueOf(product.getPrice());

            // Calcula el subtotal para cada PurchaseItem y lo suma al totalAmount
            BigDecimal subtotal = productPrice.multiply(BigDecimal.valueOf(quantity));
            totalAmount = totalAmount.add(subtotal);
        }

        purchase.setTotalAmount(totalAmount);
    }

}

