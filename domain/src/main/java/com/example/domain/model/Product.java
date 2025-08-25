package com.example.domain.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Product aggregate root.
 * Represents a product in the domain with its business rules and invariants.
 */
public class Product {
    
    @NotNull
    private final ProductId id;
    
    @NotBlank
    private String name;
    
    private String description;
    
    @NotNull
    private Money price;
    
    @Positive
    private int stockQuantity;
    
    @NotNull
    private ProductStatus status;
    
    @NotNull
    private final LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    // Constructor for creating new products
    public Product(String name, String description, Money price, int stockQuantity) {
        this.id = ProductId.generate();
        this.name = validateAndSetName(name);
        this.description = description;
        this.price = Objects.requireNonNull(price, "Price cannot be null");
        this.stockQuantity = validateAndSetStockQuantity(stockQuantity);
        this.status = ProductStatus.ACTIVE;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Constructor for loading existing products from repository
    public Product(ProductId id, String name, String description, Money price, 
                   int stockQuantity, ProductStatus status, LocalDateTime createdAt, 
                   LocalDateTime updatedAt) {
        this.id = Objects.requireNonNull(id, "Product ID cannot be null");
        this.name = validateAndSetName(name);
        this.description = description;
        this.price = Objects.requireNonNull(price, "Price cannot be null");
        this.stockQuantity = validateAndSetStockQuantity(stockQuantity);
        this.status = Objects.requireNonNull(status, "Status cannot be null");
        this.createdAt = Objects.requireNonNull(createdAt, "Created at cannot be null");
        this.updatedAt = updatedAt != null ? updatedAt : createdAt;
    }
    
    // Business methods
    public void updateName(String newName) {
        this.name = validateAndSetName(newName);
        this.updatedAt = LocalDateTime.now();
    }
    
    public void updateDescription(String newDescription) {
        this.description = newDescription;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void updatePrice(Money newPrice) {
        this.price = Objects.requireNonNull(newPrice, "Price cannot be null");
        this.updatedAt = LocalDateTime.now();
    }
    
    public void addStock(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Stock quantity to add must be positive");
        }
        this.stockQuantity += quantity;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void removeStock(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Stock quantity to remove must be positive");
        }
        if (this.stockQuantity < quantity) {
            throw new IllegalStateException("Insufficient stock available");
        }
        this.stockQuantity -= quantity;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void activate() {
        this.status = ProductStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void deactivate() {
        this.status = ProductStatus.INACTIVE;
        this.updatedAt = LocalDateTime.now();
    }
    
    public boolean isActive() {
        return status == ProductStatus.ACTIVE;
    }
    
    public boolean isInStock() {
        return stockQuantity > 0;
    }
    
    public boolean isAvailable() {
        return isActive() && isInStock();
    }
    
    // Validation methods
    private String validateAndSetName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be null or empty");
        }
        if (name.trim().length() > 100) {
            throw new IllegalArgumentException("Product name cannot exceed 100 characters");
        }
        return name.trim();
    }
    
    private int validateAndSetStockQuantity(int stockQuantity) {
        if (stockQuantity < 0) {
            throw new IllegalArgumentException("Stock quantity cannot be negative");
        }
        return stockQuantity;
    }
    
    // Getters
    public ProductId getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public Money getPrice() {
        return price;
    }
    
    public int getStockQuantity() {
        return stockQuantity;
    }
    
    public ProductStatus getStatus() {
        return status;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Product product = (Product) obj;
        return Objects.equals(id, product.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", stockQuantity=" + stockQuantity +
                ", status=" + status +
                '}';
    }
}