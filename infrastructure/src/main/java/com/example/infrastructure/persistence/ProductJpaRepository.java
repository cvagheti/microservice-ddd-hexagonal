package com.example.infrastructure.persistence;

import com.example.domain.model.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA Repository for ProductJpaEntity.
 * This interface provides CRUD operations and custom queries for product persistence.
 */
@Repository
public interface ProductJpaRepository extends JpaRepository<ProductJpaEntity, String> {
    
    /**
     * Find products by name containing the given text (case-insensitive).
     * 
     * @param name the name text to search for
     * @return a list of products whose names contain the given text
     */
    @Query("SELECT p FROM ProductJpaEntity p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<ProductJpaEntity> findByNameContainingIgnoreCase(@Param("name") String name);
    
    /**
     * Find all products with a specific status.
     * 
     * @param status the product status
     * @return a list of products with the given status
     */
    List<ProductJpaEntity> findByStatus(ProductStatus status);
    
    /**
     * Find all active products.
     * 
     * @return a list of active products
     */
    @Query("SELECT p FROM ProductJpaEntity p WHERE p.status = 'ACTIVE'")
    List<ProductJpaEntity> findActiveProducts();
    
    /**
     * Count products by status.
     * 
     * @param status the product status
     * @return the count of products with the given status
     */
    long countByStatus(ProductStatus status);
    
    /**
     * Check if a product exists with the given name (case-insensitive).
     * 
     * @param name the product name
     * @return true if a product exists with the given name, false otherwise
     */
    @Query("SELECT COUNT(p) > 0 FROM ProductJpaEntity p WHERE LOWER(p.name) = LOWER(:name)")
    boolean existsByNameIgnoreCase(@Param("name") String name);
    
    /**
     * Check if a product exists with the given name excluding a specific ID (case-insensitive).
     * 
     * @param name the product name
     * @param excludeId the ID to exclude from the search
     * @return true if a product exists with the given name (excluding the specified ID), false otherwise
     */
    @Query("SELECT COUNT(p) > 0 FROM ProductJpaEntity p WHERE LOWER(p.name) = LOWER(:name) AND p.id != :excludeId")
    boolean existsByNameIgnoreCaseAndIdNot(@Param("name") String name, @Param("excludeId") String excludeId);
}