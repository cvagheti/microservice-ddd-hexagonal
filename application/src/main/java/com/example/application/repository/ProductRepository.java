package com.example.application.repository;

import com.example.domain.model.Product;
import com.example.domain.model.ProductId;

import java.util.List;
import java.util.Optional;

/**
 * Product Repository interface.
 * This interface defines the contract for product persistence operations.
 * Following the repository pattern, it provides an abstraction layer
 * for data access operations without revealing implementation details.
 */
public interface ProductRepository {
    
    /**
     * Save a product.
     * 
     * @param product the product to save
     * @return the saved product
     */
    Product save(Product product);
    
    /**
     * Find a product by its ID.
     * 
     * @param id the product ID
     * @return an Optional containing the product if found, empty otherwise
     */
    Optional<Product> findById(ProductId id);
    
    /**
     * Find all products.
     * 
     * @return a list of all products
     */
    List<Product> findAll();
    
    /**
     * Find products by name containing the given text.
     * 
     * @param name the name text to search for
     * @return a list of products whose names contain the given text
     */
    List<Product> findByNameContaining(String name);
    
    /**
     * Find all active products.
     * 
     * @return a list of active products
     */
    List<Product> findActiveProducts();
    
    /**
     * Check if a product exists by ID.
     * 
     * @param id the product ID
     * @return true if the product exists, false otherwise
     */
    boolean existsById(ProductId id);
    
    /**
     * Delete a product by ID.
     * 
     * @param id the product ID
     */
    void deleteById(ProductId id);
    
    /**
     * Count total number of products.
     * 
     * @return the total count of products
     */
    long count();
}