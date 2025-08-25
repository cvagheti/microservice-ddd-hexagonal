package com.example.domain.model;

import jakarta.validation.constraints.NotNull;
import java.util.Objects;
import java.util.UUID;

/**
 * Product ID value object.
 * Represents the unique identifier for a Product in the domain.
 */
public class ProductId {
    
    @NotNull
    private final String value;
    
    private ProductId(String value) {
        this.value = Objects.requireNonNull(value, "Product ID cannot be null");
    }
    
    public static ProductId of(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Product ID cannot be null or empty");
        }
        return new ProductId(value.trim());
    }
    
    public static ProductId generate() {
        return new ProductId(UUID.randomUUID().toString());
    }
    
    public String getValue() {
        return value;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ProductId productId = (ProductId) obj;
        return Objects.equals(value, productId.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
    
    @Override
    public String toString() {
        return "ProductId{" + "value='" + value + '\'' + '}';
    }
}