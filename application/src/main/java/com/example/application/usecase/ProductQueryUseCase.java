package com.example.application.usecase;

import com.example.domain.model.Product;
import com.example.domain.model.ProductId;

import java.util.List;
import java.util.Optional;

/**
 * Product Query Use Case interface.
 * This interface defines the contract for product read operations (queries).
 * It represents the inbound port for product-related query operations
 * that retrieve data without modifying system state.
 * 
 * Architectural Role: Inbound Port for Queries (Hexagonal Architecture)
 */
public interface ProductQueryUseCase {

    /**
     * Query: Find a product by ID.
     *
     * @param productId the product ID
     * @return optional containing the product if found
     */
    Optional<Product> findProductById(ProductId productId);

    /**
     * Query: Find all products.
     *
     * @return list of all products
     */
    List<Product> findAllProducts();

    /**
     * Query: Find products by name.
     *
     * @param name the name to search for
     * @return list of products matching the name
     */
    List<Product> findProductsByName(String name);

    /**
     * Query: Find active products.
     *
     * @return list of active products
     */
    List<Product> findActiveProducts();

    /**
     * Query: Get inventory statistics.
     *
     * @return inventory statistics
     */
    InventoryStatistics getInventoryStatistics();

    /**
     * Inventory statistics data structure.
     */
    record InventoryStatistics(
            long totalProducts,
            long activeProducts,
            long inactiveProducts,
            long totalStock,
            double averagePrice) {
    }
}