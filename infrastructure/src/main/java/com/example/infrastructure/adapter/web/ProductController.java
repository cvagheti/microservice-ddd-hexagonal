package com.example.infrastructure.adapter.web;

import com.example.application.dto.CreateProductRequest;
import com.example.application.dto.ProductDto;
import com.example.application.dto.UpdateProductRequest;
import com.example.application.usecase.ProductCommandUseCase;
import com.example.application.usecase.ProductQueryUseCase;
import com.example.domain.model.Money;
import com.example.domain.model.Product;
import com.example.domain.model.ProductId;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST Controller for Product Management with CQRS pattern.
 * This web controller implements the REST API interface for product operations
 * using separate command and query interfaces following CQRS principles.
 * 
 * Architectural Role: Web Controller (Primary Adapter in Hexagonal Architecture)
 * - Receives HTTP requests from external clients
 * - Converts request data to domain objects
 * - Delegates command operations to ProductCommandUseCase
 * - Delegates query operations to ProductQueryUseCase
 * - Converts domain responses to DTOs for HTTP responses
 */
@RestController
@RequestMapping("/api/v1/products")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ProductController {
    
    private final ProductCommandUseCase productCommandUseCase;
    private final ProductQueryUseCase productQueryUseCase;
    
    public ProductController(ProductCommandUseCase productCommandUseCase,
                            ProductQueryUseCase productQueryUseCase) {
        this.productCommandUseCase = Objects.requireNonNull(productCommandUseCase, 
                "Product command use case cannot be null");
        this.productQueryUseCase = Objects.requireNonNull(productQueryUseCase, 
                "Product query use case cannot be null");
    }
    
    // Command operations (write)
    
    /**
     * Create a new product.
     * 
     * @param request the product creation request
     * @return the created product DTO
     */
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody CreateProductRequest request) {
        try {
            Money price = Money.of(request.getPrice(), request.getCurrency());
            Product createdProduct = productCommandUseCase.createProduct(
                    request.getName(),
                    request.getDescription(),
                    price,
                    request.getStockQuantity()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(ProductDto.fromDomain(createdProduct));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // Query operations (read)
    
    /**
     * Get a product by ID.
     * 
     * @param productId the product ID
     * @return the product DTO if found, 404 otherwise
     */
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable String productId) {
        ProductId id = ProductId.of(productId);
        Optional<Product> product = productQueryUseCase.findProductById(id);
        return product.map(p -> ResponseEntity.ok(ProductDto.fromDomain(p)))
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Get all products.
     * 
     * @return list of all product DTOs
     */
    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<Product> products = productQueryUseCase.findAllProducts();
        List<ProductDto> productDtos = products.stream()
                .map(ProductDto::fromDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productDtos);
    }
    
    /**
     * Get active products.
     * 
     * @return list of active product DTOs
     */
    @GetMapping("/active")
    public ResponseEntity<List<ProductDto>> getActiveProducts() {
        List<Product> products = productQueryUseCase.findActiveProducts();
        List<ProductDto> productDtos = products.stream()
                .map(ProductDto::fromDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productDtos);
    }
    
    /**
     * Search products by name.
     * 
     * @param name the name to search for
     * @return list of product DTOs matching the name
     */
    @GetMapping("/search")
    public ResponseEntity<List<ProductDto>> searchProductsByName(@RequestParam String name) {
        if (name == null || name.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        List<Product> products = productQueryUseCase.findProductsByName(name);
        List<ProductDto> productDtos = products.stream()
                .map(ProductDto::fromDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productDtos);
    }
    
    /**
     * Update a product.
     * 
     * @param productId the product ID
     * @param request the product update request
     * @return the updated product DTO
     */
    @PutMapping("/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable String productId,
                                                   @Valid @RequestBody UpdateProductRequest request) {
        try {
            ProductId id = ProductId.of(productId);
            Money price = Money.of(request.getPrice(), request.getCurrency());
            Product updatedProduct = productCommandUseCase.updateProduct(
                    id,
                    request.getName(),
                    request.getDescription(),
                    price
            );
            return ResponseEntity.ok(ProductDto.fromDomain(updatedProduct));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Add stock to a product.
     * 
     * @param productId the product ID
     * @param quantity the quantity to add
     * @return the updated product DTO
     */
    @PatchMapping("/{productId}/stock/add")
    public ResponseEntity<ProductDto> addStock(@PathVariable String productId,
                                              @RequestParam int quantity) {
        try {
            if (quantity <= 0) {
                return ResponseEntity.badRequest().build();
            }
            
            ProductId id = ProductId.of(productId);
            Product updatedProduct = productCommandUseCase.addStock(id, quantity);
            return ResponseEntity.ok(ProductDto.fromDomain(updatedProduct));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Remove stock from a product.
     * 
     * @param productId the product ID
     * @param quantity the quantity to remove
     * @return the updated product DTO
     */
    @PatchMapping("/{productId}/stock/remove")
    public ResponseEntity<ProductDto> removeStock(@PathVariable String productId,
                                                 @RequestParam int quantity) {
        try {
            if (quantity <= 0) {
                return ResponseEntity.badRequest().build();
            }
            
            ProductId id = ProductId.of(productId);
            Product updatedProduct = productCommandUseCase.removeStock(id, quantity);
            return ResponseEntity.ok(ProductDto.fromDomain(updatedProduct));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
    
    /**
     * Activate a product.
     * 
     * @param productId the product ID
     * @return the activated product DTO
     */
    @PatchMapping("/{productId}/activate")
    public ResponseEntity<ProductDto> activateProduct(@PathVariable String productId) {
        try {
            ProductId id = ProductId.of(productId);
            Product activatedProduct = productCommandUseCase.activateProduct(id);
            return ResponseEntity.ok(ProductDto.fromDomain(activatedProduct));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Deactivate a product.
     * 
     * @param productId the product ID
     * @return the deactivated product DTO
     */
    @PatchMapping("/{productId}/deactivate")
    public ResponseEntity<ProductDto> deactivateProduct(@PathVariable String productId) {
        try {
            ProductId id = ProductId.of(productId);
            Product deactivatedProduct = productCommandUseCase.deactivateProduct(id);
            return ResponseEntity.ok(ProductDto.fromDomain(deactivatedProduct));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Delete a product.
     * 
     * @param productId the product ID
     * @return 204 No Content if successful
     */
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String productId) {
        try {
            ProductId id = ProductId.of(productId);
            productCommandUseCase.deleteProduct(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Get inventory statistics.
     * 
     * @return inventory statistics
     */
    @GetMapping("/statistics")
    public ResponseEntity<ProductQueryUseCase.InventoryStatistics> getInventoryStatistics() {
        ProductQueryUseCase.InventoryStatistics stats = productQueryUseCase.getInventoryStatistics();
        return ResponseEntity.ok(stats);
    }
}