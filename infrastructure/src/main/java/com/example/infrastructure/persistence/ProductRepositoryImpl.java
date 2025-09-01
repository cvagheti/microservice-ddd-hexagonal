package com.example.infrastructure.persistence;

import com.example.domain.model.Product;
import com.example.domain.model.ProductId;
import com.example.domain.model.ProductStatus;
import com.example.application.repository.ProductRepository;
import com.example.infrastructure.persistence.mapper.ProductMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of ProductRepository interface.
 * This class provides the concrete implementation for product persistence operations.
 * It bridges between the application layer and the persistence infrastructure.
 */
@Repository
public class ProductRepositoryImpl implements ProductRepository {
    
    private final ProductJpaRepository jpaRepository;
    private final ProductMapper productMapper;
    
    public ProductRepositoryImpl(ProductJpaRepository jpaRepository, ProductMapper productMapper) {
        this.jpaRepository = Objects.requireNonNull(jpaRepository, 
                "JPA repository cannot be null");
        this.productMapper = Objects.requireNonNull(productMapper,
                "Product mapper cannot be null");
    }
    
    @Override
    public Product save(Product product) {
        Objects.requireNonNull(product, "Product cannot be null");
        
        ProductJpaEntity jpaEntity = productMapper.toJpaEntity(product);
        ProductJpaEntity savedEntity = jpaRepository.save(jpaEntity);
        
        return productMapper.toDomainEntity(savedEntity);
    }
    
    @Override
    public Optional<Product> findById(ProductId id) {
        Objects.requireNonNull(id, "Product ID cannot be null");
        
        return jpaRepository.findById(id.getValue())
                .map(productMapper::toDomainEntity);
    }
    
    @Override
    public List<Product> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(productMapper::toDomainEntity)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Product> findByNameContaining(String name) {
        if (name == null || name.trim().isEmpty()) {
            return List.of();
        }
        
        return jpaRepository.findByNameContainingIgnoreCase(name.trim())
                .stream()
                .map(productMapper::toDomainEntity)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Product> findActiveProducts() {
        return jpaRepository.findActiveProducts()
                .stream()
                .map(productMapper::toDomainEntity)
                .collect(Collectors.toList());
    }
    
    @Override
    public boolean existsById(ProductId id) {
        Objects.requireNonNull(id, "Product ID cannot be null");
        
        return jpaRepository.existsById(id.getValue());
    }
    
    @Override
    public void deleteById(ProductId id) {
        Objects.requireNonNull(id, "Product ID cannot be null");
        
        jpaRepository.deleteById(id.getValue());
    }
    
    @Override
    public long count() {
        return jpaRepository.count();
    }
    
    /**
     * Additional method to check if a product name exists (case-insensitive).
     * This method is used by the domain service for validation.
     * 
     * @param name the product name
     * @return true if a product exists with the given name, false otherwise
     */
    public boolean existsByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        
        return jpaRepository.existsByNameIgnoreCase(name.trim());
    }
    
    /**
     * Additional method to check if a product name exists excluding a specific ID.
     * This method is used by the domain service for validation during updates.
     * 
     * @param name the product name
     * @param excludeId the product ID to exclude from the search
     * @return true if a product exists with the given name (excluding the specified ID), false otherwise
     */
    public boolean existsByNameAndIdNot(String name, ProductId excludeId) {
        if (name == null || name.trim().isEmpty() || excludeId == null) {
            return false;
        }
        
        return jpaRepository.existsByNameIgnoreCaseAndIdNot(name.trim(), excludeId.getValue());
    }
    
    /**
     * Find products by status.
     * 
     * @param status the product status
     * @return a list of products with the given status
     */
    public List<Product> findByStatus(ProductStatus status) {
        Objects.requireNonNull(status, "Status cannot be null");
        
        return jpaRepository.findByStatus(status)
                .stream()
                .map(productMapper::toDomainEntity)
                .collect(Collectors.toList());
    }
    
    /**
     * Count products by status.
     * 
     * @param status the product status
     * @return the count of products with the given status
     */
    public long countByStatus(ProductStatus status) {
        Objects.requireNonNull(status, "Status cannot be null");
        
        return jpaRepository.countByStatus(status);
    }
}