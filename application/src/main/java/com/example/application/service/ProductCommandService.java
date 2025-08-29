package com.example.application.service;

import com.example.domain.model.Product;
import com.example.domain.model.ProductId;
import com.example.domain.model.Money;
import com.example.application.repository.ProductRepository;
import com.example.domain.service.ProductDomainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * Product command service.
 * Handles all write operations for products following CQRS pattern.
 * This service implements command use cases and coordinates domain operations.
 * 
 * Architectural Role: Application Service for Commands
 */
@Service
@Transactional
public class ProductCommandService {
    
    private final ProductRepository productRepository;
    private final ProductDomainService productDomainService;
    
    public ProductCommandService(ProductRepository productRepository, 
                                ProductDomainService productDomainService) {
        this.productRepository = Objects.requireNonNull(productRepository, 
                "Product repository cannot be null");
        this.productDomainService = Objects.requireNonNull(productDomainService, 
                "Product domain service cannot be null");
    }
    
    /**
     * Command: Create a new product.
     */
    public Product createProduct(String name, String description, Money price, int stockQuantity) {
        // Create the product entity
        Product product = new Product(name, description, price, stockQuantity);
        
        // Validate domain rules
        List<Product> existingProducts = productRepository.findByNameContaining(product.getName());
        productDomainService.validateProductForCreation(product, existingProducts);
        
        // Save and return
        return productRepository.save(product);
    }
    
    /**
     * Command: Update an existing product.
     */
    public Product updateProduct(ProductId productId, String name, String description, Money price) {
        // Find existing product
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Product not found with ID: " + productId.getValue()));
        
        // Update product attributes
        product.updateName(name);
        product.updateDescription(description);
        product.updatePrice(price);
        
        // Validate domain rules
        List<Product> existingProducts = productRepository.findByNameContaining(product.getName());
        boolean productExists = productRepository.existsById(product.getId());
        productDomainService.validateProductForUpdate(product, existingProducts, productExists);
        
        // Save and return
        return productRepository.save(product);
    }
    
    /**
     * Command: Add stock to a product.
     */
    public Product addStock(ProductId productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Product not found with ID: " + productId.getValue()));
        
        product.addStock(quantity);
        return productRepository.save(product);
    }
    
    /**
     * Command: Remove stock from a product.
     */
    public Product removeStock(ProductId productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Product not found with ID: " + productId.getValue()));
        
        product.removeStock(quantity);
        return productRepository.save(product);
    }
    
    /**
     * Command: Activate a product.
     */
    public Product activateProduct(ProductId productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Product not found with ID: " + productId.getValue()));
        
        product.activate();
        return productRepository.save(product);
    }
    
    /**
     * Command: Deactivate a product.
     */
    public Product deactivateProduct(ProductId productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Product not found with ID: " + productId.getValue()));
        
        product.deactivate();
        return productRepository.save(product);
    }
    
    /**
     * Command: Delete a product.
     */
    public void deleteProduct(ProductId productId) {
        boolean productExists = productRepository.existsById(productId);
        if (!productDomainService.canDeleteProduct(productId, productExists)) {
            throw new IllegalArgumentException(
                    "Product cannot be deleted: " + productId.getValue());
        }
        
        productRepository.deleteById(productId);
    }
}