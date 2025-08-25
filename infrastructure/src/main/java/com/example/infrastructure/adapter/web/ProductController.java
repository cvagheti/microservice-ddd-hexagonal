package com.example.infrastructure.adapter.web;

import com.example.application.dto.CreateProductRequest;
import com.example.application.dto.ProductDto;
import com.example.application.dto.UpdateProductRequest;
import com.example.application.port.in.ProductManagementUseCase;
import com.example.domain.service.ProductDomainService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST Controller for Product Management.
 * This adapter implements the web interface for product operations.
 * It translates HTTP requests to use case calls and handles responses.
 */
@RestController
@RequestMapping("/api/v1/products")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ProductController {
    
    private final ProductManagementUseCase productManagementUseCase;
    
    public ProductController(ProductManagementUseCase productManagementUseCase) {
        this.productManagementUseCase = Objects.requireNonNull(productManagementUseCase, 
                "Product management use case cannot be null");
    }
    
    /**
     * Create a new product.
     * 
     * @param request the product creation request
     * @return the created product DTO
     */
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody CreateProductRequest request) {
        try {
            ProductDto createdProduct = productManagementUseCase.createProduct(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Get a product by ID.
     * 
     * @param productId the product ID
     * @return the product DTO if found, 404 otherwise
     */
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable String productId) {
        Optional<ProductDto> product = productManagementUseCase.findProductById(productId);
        return product.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Get all products.
     * 
     * @return list of all product DTOs
     */
    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<ProductDto> products = productManagementUseCase.findAllProducts();
        return ResponseEntity.ok(products);
    }
    
    /**
     * Get active products.
     * 
     * @return list of active product DTOs
     */
    @GetMapping("/active")
    public ResponseEntity<List<ProductDto>> getActiveProducts() {
        List<ProductDto> products = productManagementUseCase.findActiveProducts();
        return ResponseEntity.ok(products);
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
        
        List<ProductDto> products = productManagementUseCase.findProductsByName(name);
        return ResponseEntity.ok(products);
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
            ProductDto updatedProduct = productManagementUseCase.updateProduct(productId, request);
            return ResponseEntity.ok(updatedProduct);
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
            
            ProductDto updatedProduct = productManagementUseCase.addStock(productId, quantity);
            return ResponseEntity.ok(updatedProduct);
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
            
            ProductDto updatedProduct = productManagementUseCase.removeStock(productId, quantity);
            return ResponseEntity.ok(updatedProduct);
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
            ProductDto activatedProduct = productManagementUseCase.activateProduct(productId);
            return ResponseEntity.ok(activatedProduct);
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
            ProductDto deactivatedProduct = productManagementUseCase.deactivateProduct(productId);
            return ResponseEntity.ok(deactivatedProduct);
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
            productManagementUseCase.deleteProduct(productId);
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
    public ResponseEntity<ProductDomainService.InventoryStatistics> getInventoryStatistics() {
        ProductDomainService.InventoryStatistics stats = productManagementUseCase.getInventoryStatistics();
        return ResponseEntity.ok(stats);
    }
}