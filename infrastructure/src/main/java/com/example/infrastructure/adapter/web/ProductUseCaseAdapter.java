package com.example.infrastructure.adapter.web;

import com.example.application.dto.CreateProductRequest;
import com.example.application.dto.ProductDto;
import com.example.application.dto.UpdateProductRequest;
import com.example.application.usecase.ProductManagementUseCase;
import com.example.application.service.ProductApplicationService;
import com.example.domain.model.Money;
import com.example.domain.model.Product;
import com.example.domain.model.ProductId;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Use Case Adapter that implements the ProductManagementUseCase interface.
 * This adapter bridges between the web layer and the application service.
 */
@Component
public class ProductUseCaseAdapter implements ProductManagementUseCase {
    
    private final ProductApplicationService productApplicationService;
    
    public ProductUseCaseAdapter(ProductApplicationService productApplicationService) {
        this.productApplicationService = Objects.requireNonNull(productApplicationService, 
                "Product application service cannot be null");
    }
    
    @Override
    public Product createProduct(String name, String description, Money price, int stockQuantity) {
        return productApplicationService.createProduct(name, description, price, stockQuantity);
    }
    
    @Override
    public Product updateProduct(ProductId productId, String name, String description, Money price) {
        return productApplicationService.updateProduct(productId, name, description, price);
    }
    
    @Override
    public Product addStock(ProductId productId, int quantity) {
        return productApplicationService.addStock(productId, quantity);
    }
    
    @Override
    public Product removeStock(ProductId productId, int quantity) {
        return productApplicationService.removeStock(productId, quantity);
    }
    
    @Override
    public Product activateProduct(ProductId productId) {
        return productApplicationService.activateProduct(productId);
    }
    
    @Override
    public Product deactivateProduct(ProductId productId) {
        return productApplicationService.deactivateProduct(productId);
    }
    
    @Override
    public void deleteProduct(ProductId productId) {
        productApplicationService.deleteProduct(productId);
    }
    
    @Override
    public Optional<Product> findProductById(ProductId productId) {
        return productApplicationService.findProductById(productId);
    }
    
    @Override
    public List<Product> findAllProducts() {
        return productApplicationService.findAllProducts();
    }
    
    @Override
    public List<Product> findProductsByName(String name) {
        return productApplicationService.findProductsByName(name);
    }
    
    @Override
    public List<Product> findActiveProducts() {
        return productApplicationService.findActiveProducts();
    }
    
    @Override
    public ProductManagementUseCase.InventoryStatistics getInventoryStatistics() {
        var stats = productApplicationService.getInventoryStatistics();
        return new ProductManagementUseCase.InventoryStatistics(
            stats.getTotalProducts(),
            stats.getActiveProducts(),
            stats.getInactiveProducts(),
            stats.getProductsInStock(), // Using this as totalStock
            0.0 // averagePrice not available in domain service, setting to 0
        );
    }
}