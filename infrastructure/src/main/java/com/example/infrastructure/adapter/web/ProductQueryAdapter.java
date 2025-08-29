package com.example.infrastructure.adapter.web;

import com.example.application.usecase.ProductQueryUseCase;
import com.example.application.service.ProductQueryService;
import com.example.domain.model.Product;
import com.example.domain.model.ProductId;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Product Query Adapter.
 * This adapter implements the ProductQueryUseCase interface and bridges 
 * between the web layer and the query application service.
 * 
 * Architectural Role: Primary Adapter for Queries (Hexagonal Architecture)
 */
@Component
public class ProductQueryAdapter implements ProductQueryUseCase {
    
    private final ProductQueryService productQueryService;
    
    public ProductQueryAdapter(ProductQueryService productQueryService) {
        this.productQueryService = Objects.requireNonNull(productQueryService, 
                "Product query service cannot be null");
    }
    
    @Override
    public Optional<Product> findProductById(ProductId productId) {
        return productQueryService.findProductById(productId);
    }
    
    @Override
    public List<Product> findAllProducts() {
        return productQueryService.findAllProducts();
    }
    
    @Override
    public List<Product> findProductsByName(String name) {
        return productQueryService.findProductsByName(name);
    }
    
    @Override
    public List<Product> findActiveProducts() {
        return productQueryService.findActiveProducts();
    }
    
    @Override
    public InventoryStatistics getInventoryStatistics() {
        return productQueryService.getInventoryStatistics();
    }
}