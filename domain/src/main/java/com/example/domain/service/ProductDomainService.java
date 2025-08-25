package com.example.domain.service;

import com.example.domain.model.Product;
import com.example.domain.model.ProductId;
import com.example.domain.repository.ProductRepository;
import java.util.List;
import java.util.Objects;

/**
 * Product domain service.
 * Contains complex business logic that doesn't naturally fit within a single entity.
 * This service coordinates business operations across multiple domain objects.
 */
public class ProductDomainService {
    
    private final ProductRepository productRepository;
    
    public ProductDomainService(ProductRepository productRepository) {
        this.productRepository = Objects.requireNonNull(productRepository, 
                "Product repository cannot be null");
    }
    
    /**
     * Checks if a product name is unique in the system.
     * This is a domain rule that spans across multiple products.
     * 
     * @param name the product name to check
     * @param excludeProductId optional product ID to exclude from the check (for updates)
     * @return true if the name is unique, false otherwise
     */
    public boolean isProductNameUnique(String name, ProductId excludeProductId) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        
        List<Product> productsWithSimilarName = productRepository.findByNameContaining(name.trim());
        
        return productsWithSimilarName.stream()
                .filter(product -> product.getName().equalsIgnoreCase(name.trim()))
                .noneMatch(product -> excludeProductId == null || 
                          !product.getId().equals(excludeProductId));
    }
    
    /**
     * Validates business rules before creating a new product.
     * 
     * @param product the product to validate
     * @throws IllegalArgumentException if validation fails
     */
    public void validateProductForCreation(Product product) {
        Objects.requireNonNull(product, "Product cannot be null");
        
        if (!isProductNameUnique(product.getName(), null)) {
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
     * @throws IllegalArgumentException if validation fails
     */
    public void validateProductForUpdate(Product product) {
        Objects.requireNonNull(product, "Product cannot be null");
        
        if (!productRepository.existsById(product.getId())) {
            throw new IllegalArgumentException(
                    "Product with ID " + product.getId().getValue() + " does not exist");
        }
        
        if (!isProductNameUnique(product.getName(), product.getId())) {
            throw new IllegalArgumentException(
                    "Product name '" + product.getName() + "' already exists");
        }
    }
    
    /**
     * Determines if a product can be safely deleted.
     * In a real application, this might check for references in orders, etc.
     * 
     * @param productId the product ID to check
     * @return true if the product can be deleted, false otherwise
     */
    public boolean canDeleteProduct(ProductId productId) {
        Objects.requireNonNull(productId, "Product ID cannot be null");
        
        // In a real application, you would check for business constraints
        // such as existing orders, references in other aggregates, etc.
        return productRepository.existsById(productId);
    }
    
    /**
     * Calculates inventory statistics for reporting purposes.
     * This is an example of domain logic that operates on multiple products.
     * 
     * @return inventory statistics
     */
    public InventoryStatistics calculateInventoryStatistics() {
        List<Product> allProducts = productRepository.findAll();
        List<Product> activeProducts = productRepository.findActiveProducts();
        
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