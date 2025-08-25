package com.example.domain.repository;

import com.example.domain.model.Product;
import com.example.domain.model.ProductId;
import java.util.List;
import java.util.Optional;

/**
 * Product repository interface.
 * Defines the contract for Product persistence operations in the domain.
 * This interface belongs to the domain layer and will be implemented in the infrastructure layer.
 */
public interface ProductRepository {
    
    /**
     * Save a product to the repository.
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