package com.example.application.port.in;

import com.example.application.dto.CreateProductRequest;
import com.example.application.dto.ProductDto;
import com.example.application.dto.UpdateProductRequest;
import com.example.domain.service.ProductDomainService;

import java.util.List;
import java.util.Optional;

/**
 * Product Management Use Cases - Inbound Port.
 * This interface defines the use cases that the application layer exposes.
 * It represents the primary ports in hexagonal architecture.
 */
public interface ProductManagementUseCase {
    
    /**
     * Create a new product.
     * 
     * @param request the product creation request
     * @return the created product DTO
     */
    ProductDto createProduct(CreateProductRequest request);
    
    /**
     * Update an existing product.
     * 
     * @param productId the product ID
     * @param request the product update request
     * @return the updated product DTO
     */
    ProductDto updateProduct(String productId, UpdateProductRequest request);
    
    /**
     * Add stock to a product.
     * 
     * @param productId the product ID
     * @param quantity quantity to add
     * @return the updated product DTO
     */
    ProductDto addStock(String productId, int quantity);
    
    /**
     * Remove stock from a product.
     * 
     * @param productId the product ID
     * @param quantity quantity to remove
     * @return the updated product DTO
     */
    ProductDto removeStock(String productId, int quantity);
    
    /**
     * Activate a product.
     * 
     * @param productId the product ID
     * @return the activated product DTO
     */
    ProductDto activateProduct(String productId);
    
    /**
     * Deactivate a product.
     * 
     * @param productId the product ID
     * @return the deactivated product DTO
     */
    ProductDto deactivateProduct(String productId);
    
    /**
     * Delete a product.
     * 
     * @param productId the product ID
     */
    void deleteProduct(String productId);
    
    /**
     * Find a product by ID.
     * 
     * @param productId the product ID
     * @return optional containing the product DTO if found
     */
    Optional<ProductDto> findProductById(String productId);
    
    /**
     * Find all products.
     * 
     * @return list of all product DTOs
     */
    List<ProductDto> findAllProducts();
    
    /**
     * Find products by name.
     * 
     * @param name the name to search for
     * @return list of product DTOs matching the name
     */
    List<ProductDto> findProductsByName(String name);
    
    /**
     * Find all active products.
     * 
     * @return list of active product DTOs
     */
    List<ProductDto> findActiveProducts();
    
    /**
     * Get inventory statistics.
     * 
     * @return inventory statistics
     */
    ProductDomainService.InventoryStatistics getInventoryStatistics();
}