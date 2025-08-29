package com.example.application.usecase;

import com.example.domain.model.Product;
import com.example.domain.model.ProductId;
import com.example.domain.model.Money;

/**
 * Product Command Use Case interface.
 * This interface defines the contract for product write operations (commands).
 * It represents the inbound port for product-related command operations
 * that modify the system state.
 * 
 * Architectural Role: Inbound Port for Commands (Hexagonal Architecture)
 */
public interface ProductCommandUseCase {

    /**
     * Command: Create a new product.
     *
     * @param name product name
     * @param description product description  
     * @param price product price
     * @param stockQuantity initial stock quantity
     * @return the created product
     */
    Product createProduct(String name, String description, Money price, int stockQuantity);

    /**
     * Command: Update an existing product.
     *
     * @param productId the product ID
     * @param name new product name
     * @param description new product description
     * @param price new product price
     * @return the updated product
     */
    Product updateProduct(ProductId productId, String name, String description, Money price);

    /**
     * Command: Add stock to a product.
     *
     * @param productId the product ID
     * @param quantity quantity to add
     * @return the updated product
     */
    Product addStock(ProductId productId, int quantity);

    /**
     * Command: Remove stock from a product.
     *
     * @param productId the product ID
     * @param quantity quantity to remove
     * @return the updated product
     */
    Product removeStock(ProductId productId, int quantity);

    /**
     * Command: Activate a product.
     *
     * @param productId the product ID
     * @return the activated product
     */
    Product activateProduct(ProductId productId);

    /**
     * Command: Deactivate a product.
     *
     * @param productId the product ID
     * @return the deactivated product
     */
    Product deactivateProduct(ProductId productId);

    /**
     * Command: Delete a product.
     *
     * @param productId the product ID
     */
    void deleteProduct(ProductId productId);
}