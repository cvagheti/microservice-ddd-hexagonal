package com.example.application.service;

import com.example.domain.model.Product;
import com.example.domain.model.ProductId;
import com.example.domain.model.Money;
import com.example.domain.repository.ProductRepository;
import com.example.domain.service.ProductDomainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Product application service.
 * Orchestrates domain operations and coordinates between domain objects.
 * This service implements use cases and application-specific business logic.
 */
@Service
@Transactional
public class ProductApplicationService {
    
    private final ProductRepository productRepository;
    private final ProductDomainService productDomainService;
    
    public ProductApplicationService(ProductRepository productRepository, 
                                   ProductDomainService productDomainService) {
        this.productRepository = Objects.requireNonNull(productRepository, 
                "Product repository cannot be null");
        this.productDomainService = Objects.requireNonNull(productDomainService, 
                "Product domain service cannot be null");
    }
    
    /**
     * Use case: Create a new product.
     * 
     * @param name product name
     * @param description product description
     * @param price product price
     * @param stockQuantity initial stock quantity
     * @return the created product
     */
    public Product createProduct(String name, String description, Money price, int stockQuantity) {
        // Create the product entity
        Product product = new Product(name, description, price, stockQuantity);
        
        // Validate domain rules
        productDomainService.validateProductForCreation(product);
        
        // Save and return
        return productRepository.save(product);
    }
    
    /**
     * Use case: Update an existing product.
     * 
     * @param productId the product ID
     * @param name new product name
     * @param description new product description
     * @param price new product price
     * @return the updated product
     * @throws IllegalArgumentException if product not found
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
        productDomainService.validateProductForUpdate(product);
        
        // Save and return
        return productRepository.save(product);
    }
    
    /**
     * Use case: Add stock to a product.
     * 
     * @param productId the product ID
     * @param quantity quantity to add
     * @return the updated product
     * @throws IllegalArgumentException if product not found
     */
    public Product addStock(ProductId productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Product not found with ID: " + productId.getValue()));
        
        product.addStock(quantity);
        return productRepository.save(product);
    }
    
    /**
     * Use case: Remove stock from a product.
     * 
     * @param productId the product ID
     * @param quantity quantity to remove
     * @return the updated product
     * @throws IllegalArgumentException if product not found
     * @throws IllegalStateException if insufficient stock
     */
    public Product removeStock(ProductId productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Product not found with ID: " + productId.getValue()));
        
        product.removeStock(quantity);
        return productRepository.save(product);
    }
    
    /**
     * Use case: Activate a product.
     * 
     * @param productId the product ID
     * @return the activated product
     * @throws IllegalArgumentException if product not found
     */
    public Product activateProduct(ProductId productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Product not found with ID: " + productId.getValue()));
        
        product.activate();
        return productRepository.save(product);
    }
    
    /**
     * Use case: Deactivate a product.
     * 
     * @param productId the product ID
     * @return the deactivated product
     * @throws IllegalArgumentException if product not found
     */
    public Product deactivateProduct(ProductId productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Product not found with ID: " + productId.getValue()));
        
        product.deactivate();
        return productRepository.save(product);
    }
    
    /**
     * Use case: Delete a product.
     * 
     * @param productId the product ID
     * @throws IllegalArgumentException if product not found or cannot be deleted
     */
    public void deleteProduct(ProductId productId) {
        if (!productDomainService.canDeleteProduct(productId)) {
            throw new IllegalArgumentException(
                    "Product cannot be deleted: " + productId.getValue());
        }
        
        productRepository.deleteById(productId);
    }
    
    /**
     * Query: Find a product by ID.
     * 
     * @param productId the product ID
     * @return optional containing the product if found
     */
    @Transactional(readOnly = true)
    public Optional<Product> findProductById(ProductId productId) {
        return productRepository.findById(productId);
    }
    
    /**
     * Query: Find all products.
     * 
     * @return list of all products
     */
    @Transactional(readOnly = true)
    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }
    
    /**
     * Query: Find products by name.
     * 
     * @param name the name to search for
     * @return list of products matching the name
     */
    @Transactional(readOnly = true)
    public List<Product> findProductsByName(String name) {
        return productRepository.findByNameContaining(name);
    }
    
    /**
     * Query: Find all active products.
     * 
     * @return list of active products
     */
    @Transactional(readOnly = true)
    public List<Product> findActiveProducts() {
        return productRepository.findActiveProducts();
    }
    
    /**
     * Query: Get inventory statistics.
     * 
     * @return inventory statistics
     */
    @Transactional(readOnly = true)
    public ProductDomainService.InventoryStatistics getInventoryStatistics() {
        return productDomainService.calculateInventoryStatistics();
    }
}