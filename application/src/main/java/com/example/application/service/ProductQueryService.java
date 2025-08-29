package com.example.application.service;

import com.example.domain.model.Product;
import com.example.domain.model.ProductId;
import com.example.application.repository.ProductRepository;
import com.example.domain.service.ProductDomainService;
import com.example.application.usecase.ProductQueryUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Product query service.
 * Handles all read operations for products following CQRS pattern.
 * This service implements query use cases and focuses on data retrieval.
 * 
 * Architectural Role: Application Service for Queries
 */
@Service
@Transactional(readOnly = true)
public class ProductQueryService {
    
    private final ProductRepository productRepository;
    private final ProductDomainService productDomainService;
    
    public ProductQueryService(ProductRepository productRepository,
                              ProductDomainService productDomainService) {
        this.productRepository = Objects.requireNonNull(productRepository, 
                "Product repository cannot be null");
        this.productDomainService = Objects.requireNonNull(productDomainService, 
                "Product domain service cannot be null");
    }
    
    /**
     * Query: Find a product by ID.
     */
    public Optional<Product> findProductById(ProductId productId) {
        return productRepository.findById(productId);
    }
    
    /**
     * Query: Find all products.
     */
    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }
    
    /**
     * Query: Find products by name.
     */
    public List<Product> findProductsByName(String name) {
        return productRepository.findByNameContaining(name);
    }
    
    /**
     * Query: Find all active products.
     */
    public List<Product> findActiveProducts() {
        return productRepository.findActiveProducts();
    }
    
    /**
     * Query: Get inventory statistics.
     */
    public ProductQueryUseCase.InventoryStatistics getInventoryStatistics() {
        List<Product> allProducts = productRepository.findAll();
        List<Product> activeProducts = productRepository.findActiveProducts();
        
        ProductDomainService.InventoryStatistics domainStats = 
                productDomainService.calculateInventoryStatistics(allProducts, activeProducts);
        
        return new ProductQueryUseCase.InventoryStatistics(
                domainStats.getTotalProducts(),
                domainStats.getActiveProducts(),
                domainStats.getInactiveProducts(),
                domainStats.getProductsInStock(),
                0.0 // averagePrice calculation would be added later if needed
        );
    }
}