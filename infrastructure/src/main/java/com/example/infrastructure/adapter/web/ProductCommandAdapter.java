package com.example.infrastructure.adapter.web;

import com.example.application.usecase.ProductCommandUseCase;
import com.example.application.service.ProductCommandService;
import com.example.domain.model.Money;
import com.example.domain.model.Product;
import com.example.domain.model.ProductId;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Product Command Adapter.
 * This adapter implements the ProductCommandUseCase interface and bridges 
 * between the web layer and the command application service.
 * 
 * Architectural Role: Primary Adapter for Commands (Hexagonal Architecture)
 */
@Component
public class ProductCommandAdapter implements ProductCommandUseCase {
    
    private final ProductCommandService productCommandService;
    
    public ProductCommandAdapter(ProductCommandService productCommandService) {
        this.productCommandService = Objects.requireNonNull(productCommandService, 
                "Product command service cannot be null");
    }
    
    @Override
    public Product createProduct(String name, String description, Money price, int stockQuantity) {
        return productCommandService.createProduct(name, description, price, stockQuantity);
    }
    
    @Override
    public Product updateProduct(ProductId productId, String name, String description, Money price) {
        return productCommandService.updateProduct(productId, name, description, price);
    }
    
    @Override
    public Product addStock(ProductId productId, int quantity) {
        return productCommandService.addStock(productId, quantity);
    }
    
    @Override
    public Product removeStock(ProductId productId, int quantity) {
        return productCommandService.removeStock(productId, quantity);
    }
    
    @Override
    public Product activateProduct(ProductId productId) {
        return productCommandService.activateProduct(productId);
    }
    
    @Override
    public Product deactivateProduct(ProductId productId) {
        return productCommandService.deactivateProduct(productId);
    }
    
    @Override
    public void deleteProduct(ProductId productId) {
        productCommandService.deleteProduct(productId);
    }
}