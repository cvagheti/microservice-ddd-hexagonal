package com.example.application.usecase;

import com.example.domain.model.Product;
import com.example.domain.model.ProductId;
import com.example.domain.model.Money;

import java.util.List;
import java.util.Optional;

/**
 * Product Management Use Case interface.
 * This interface defines the contract for product management operations.
 * It represents the inbound port for product-related business operations
 * that can be triggered by external adapters (like REST controllers).
 */
public interface ProductManagementUseCase {

    /**
     * Use case: Create a new product.
     *
     * @param name product name
     * @param description product description  
     * @param price product price
     * @param stockQuantity initial stock quantity
     * @return the created product
     */
    Product createProduct(String name, String description, Money price, int stockQuantity);

    /**
     * Use case: Update an existing product.
     *
     * @param productId the product ID
     * @param name new product name
     * @param description new product description
     * @param price new product price
     * @return the updated product
     */
    Product updateProduct(ProductId productId, String name, String description, Money price);

    /**
     * Use case: Add stock to a product.
     *
     * @param productId the product ID
     * @param quantity quantity to add
     * @return the updated product
     */
    Product addStock(ProductId productId, int quantity);

    /**
     * Use case: Remove stock from a product.
     *
     * @param productId the product ID
     * @param quantity quantity to remove
     * @return the updated product
     */
    Product removeStock(ProductId productId, int quantity);

    /**
     * Use case: Activate a product.
     *
     * @param productId the product ID
     * @return the activated product
     */
    Product activateProduct(ProductId productId);

    /**
     * Use case: Deactivate a product.
     *
     * @param productId the product ID
     * @return the deactivated product
     */
    Product deactivateProduct(ProductId productId);

    /**
     * Use case: Delete a product.
     *
     * @param productId the product ID
     */
    void deleteProduct(ProductId productId);

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