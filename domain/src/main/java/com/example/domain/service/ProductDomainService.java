package com.example.domain.service;

import com.example.domain.model.Product;
import com.example.domain.model.ProductId;
import java.util.List;
import java.util.Objects;

/**
 * Product domain service.
 * Contains pure business logic that doesn't naturally fit within a single entity.
 * This service coordinates business operations and validates domain rules using data provided as parameters.
 * Following DDD principles, this service has no infrastructure dependencies.
 */
public class ProductDomainService {
    
    /**
     * Checks if a product name is unique among the provided list of products.
     * This is a pure domain rule that operates on data provided as parameters.
     * 
     * @param name the product name to check
     * @param existingProducts list of existing products to check against
     * @param excludeProductId optional product ID to exclude from the check (for updates)
     * @return true if the name is unique, false otherwise
     */
    public boolean isProductNameUnique(String name, List<Product> existingProducts, ProductId excludeProductId) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        
        return existingProducts.stream()
                .filter(product -> product.getName().equalsIgnoreCase(name.trim()))
                .noneMatch(product -> excludeProductId == null || 
                          !product.getId().equals(excludeProductId));
    }
    
    /**
     * Validates business rules before creating a new product.
     * 
     * @param product the product to validate
     * @param existingProducts list of existing products to check against
     * @throws IllegalArgumentException if validation fails
     */
    public void validateProductForCreation(Product product, List<Product> existingProducts) {
        Objects.requireNonNull(product, "Product cannot be null");
        Objects.requireNonNull(existingProducts, "Existing products list cannot be null");
        
        if (!isProductNameUnique(product.getName(), existingProducts, null)) {
            throw new IllegalArgumentException(
                    "Product name '" + product.getName() + "' already exists");
        }
        
        if (!product.isActive()) {
            throw new IllegalArgumentException("New products must be created as active");
        }
    }
    
    /**
     * Validates business rules before updating an existing product.
     * 
     * @param product the product to validate
     * @param existingProducts list of existing products to check against
     * @param productExists whether the product exists in the system
     * @throws IllegalArgumentException if validation fails
     */
    public void validateProductForUpdate(Product product, List<Product> existingProducts, boolean productExists) {
        Objects.requireNonNull(product, "Product cannot be null");
        Objects.requireNonNull(existingProducts, "Existing products list cannot be null");
        
        if (!productExists) {
            throw new IllegalArgumentException(
                    "Product with ID " + product.getId().getValue() + " does not exist");
        }
        
        if (!isProductNameUnique(product.getName(), existingProducts, product.getId())) {
            throw new IllegalArgumentException(
                    "Product name '" + product.getName() + "' already exists");
        }
    }
    
    /**
     * Determines if a product can be safely deleted based on business rules.
     * In a real application, this might check for references in orders, etc.
     * 
     * @param productId the product ID to check
     * @param productExists whether the product exists in the system
     * @return true if the product can be deleted, false otherwise
     */
    public boolean canDeleteProduct(ProductId productId, boolean productExists) {
        Objects.requireNonNull(productId, "Product ID cannot be null");
        
        // In a real application, you would check for business constraints
        // such as existing orders, references in other aggregates, etc.
        return productExists;
    }
    
    /**
     * Calculates inventory statistics for reporting purposes.
     * This is an example of domain logic that operates on multiple products.
     * 
     * @param allProducts list of all products in the system
     * @param activeProducts list of active products in the system
     * @return inventory statistics
     */
    public InventoryStatistics calculateInventoryStatistics(List<Product> allProducts, List<Product> activeProducts) {
        Objects.requireNonNull(allProducts, "All products list cannot be null");
        Objects.requireNonNull(activeProducts, "Active products list cannot be null");
        
        long totalProducts = allProducts.size();
        long activeProductCount = activeProducts.size();
        long inactiveProductCount = totalProducts - activeProductCount;
        long productsInStock = activeProducts.stream()
                .mapToLong(product -> product.isInStock() ? 1 : 0)
                .sum();
        long productsOutOfStock = activeProductCount - productsInStock;
        
        return new InventoryStatistics(
                totalProducts,
                activeProductCount,
                inactiveProductCount,
                productsInStock,
                productsOutOfStock
        );
    }
    
    /**
     * Inner class representing inventory statistics.
     * This is a simple value object for reporting purposes.
     */
    public static class InventoryStatistics {
        private final long totalProducts;
        private final long activeProducts;
        private final long inactiveProducts;
        private final long productsInStock;
        private final long productsOutOfStock;
        
        public InventoryStatistics(long totalProducts, long activeProducts, 
                                 long inactiveProducts, long productsInStock, 
                                 long productsOutOfStock) {
            this.totalProducts = totalProducts;
            this.activeProducts = activeProducts;
            this.inactiveProducts = inactiveProducts;
            this.productsInStock = productsInStock;
            this.productsOutOfStock = productsOutOfStock;
        }
        
        // Getters
        public long getTotalProducts() { return totalProducts; }
        public long getActiveProducts() { return activeProducts; }
        public long getInactiveProducts() { return inactiveProducts; }
        public long getProductsInStock() { return productsInStock; }
        public long getProductsOutOfStock() { return productsOutOfStock; }
        
        @Override
        public String toString() {
            return "InventoryStatistics{" +
                    "totalProducts=" + totalProducts +
                    ", activeProducts=" + activeProducts +
                    ", inactiveProducts=" + inactiveProducts +
                    ", productsInStock=" + productsInStock +
                    ", productsOutOfStock=" + productsOutOfStock +
                    '}';
        }
    }
}