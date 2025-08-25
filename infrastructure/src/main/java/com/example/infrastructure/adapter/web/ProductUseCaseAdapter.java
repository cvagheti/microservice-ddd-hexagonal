package com.example.infrastructure.adapter.web;

import com.example.application.dto.CreateProductRequest;
import com.example.application.dto.ProductDto;
import com.example.application.dto.UpdateProductRequest;
import com.example.application.port.in.ProductManagementUseCase;
import com.example.application.service.ProductApplicationService;
import com.example.domain.model.Money;
import com.example.domain.model.Product;
import com.example.domain.model.ProductId;
import com.example.domain.service.ProductDomainService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Use Case Adapter that implements the ProductManagementUseCase port.
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
    public ProductDto createProduct(CreateProductRequest request) {
        Objects.requireNonNull(request, "Create product request cannot be null");
        
        Money price = Money.of(request.getPrice(), request.getCurrency());
        Product createdProduct = productApplicationService.createProduct(
                request.getName(),
                request.getDescription(),
                price,
                request.getStockQuantity()
        );
        
        return ProductDto.fromDomain(createdProduct);
    }
    
    @Override
    public ProductDto updateProduct(String productId, UpdateProductRequest request) {
        Objects.requireNonNull(productId, "Product ID cannot be null");
        Objects.requireNonNull(request, "Update product request cannot be null");
        
        ProductId id = ProductId.of(productId);
        Money price = Money.of(request.getPrice(), request.getCurrency());
        
        Product updatedProduct = productApplicationService.updateProduct(
                id,
                request.getName(),
                request.getDescription(),
                price
        );
        
        return ProductDto.fromDomain(updatedProduct);
    }
    
    @Override
    public ProductDto addStock(String productId, int quantity) {
        Objects.requireNonNull(productId, "Product ID cannot be null");
        
        ProductId id = ProductId.of(productId);
        Product updatedProduct = productApplicationService.addStock(id, quantity);
        
        return ProductDto.fromDomain(updatedProduct);
    }
    
    @Override
    public ProductDto removeStock(String productId, int quantity) {
        Objects.requireNonNull(productId, "Product ID cannot be null");
        
        ProductId id = ProductId.of(productId);
        Product updatedProduct = productApplicationService.removeStock(id, quantity);
        
        return ProductDto.fromDomain(updatedProduct);
    }
    
    @Override
    public ProductDto activateProduct(String productId) {
        Objects.requireNonNull(productId, "Product ID cannot be null");
        
        ProductId id = ProductId.of(productId);
        Product activatedProduct = productApplicationService.activateProduct(id);
        
        return ProductDto.fromDomain(activatedProduct);
    }
    
    @Override
    public ProductDto deactivateProduct(String productId) {
        Objects.requireNonNull(productId, "Product ID cannot be null");
        
        ProductId id = ProductId.of(productId);
        Product deactivatedProduct = productApplicationService.deactivateProduct(id);
        
        return ProductDto.fromDomain(deactivatedProduct);
    }
    
    @Override
    public void deleteProduct(String productId) {
        Objects.requireNonNull(productId, "Product ID cannot be null");
        
        ProductId id = ProductId.of(productId);
        productApplicationService.deleteProduct(id);
    }
    
    @Override
    public Optional<ProductDto> findProductById(String productId) {
        Objects.requireNonNull(productId, "Product ID cannot be null");
        
        ProductId id = ProductId.of(productId);
        return productApplicationService.findProductById(id)
                .map(ProductDto::fromDomain);
    }
    
    @Override
    public List<ProductDto> findAllProducts() {
        return productApplicationService.findAllProducts()
                .stream()
                .map(ProductDto::fromDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<ProductDto> findProductsByName(String name) {
        Objects.requireNonNull(name, "Name cannot be null");
        
        return productApplicationService.findProductsByName(name)
                .stream()
                .map(ProductDto::fromDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<ProductDto> findActiveProducts() {
        return productApplicationService.findActiveProducts()
                .stream()
                .map(ProductDto::fromDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public ProductDomainService.InventoryStatistics getInventoryStatistics() {
        return productApplicationService.getInventoryStatistics();
    }
}