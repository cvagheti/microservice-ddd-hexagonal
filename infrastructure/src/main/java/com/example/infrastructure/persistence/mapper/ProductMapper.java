package com.example.infrastructure.persistence.mapper;

import com.example.domain.model.Product;
import com.example.domain.model.ProductId;
import com.example.domain.model.Money;
import com.example.infrastructure.persistence.ProductJpaEntity;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Mapper para conversão entre Product (domain) e ProductJpaEntity (infrastructure).
 * 
 * Responsabilidades:
 * - Converter domain entities para JPA entities
 * - Converter JPA entities para domain entities
 * - Centralizar lógica de mapeamento
 * - Manter separação de responsabilidades
 * 
 * Architectural Role: Infrastructure Component (Mapper)
 */
@Component
public class ProductMapper {
    
    /**
     * Converte domain entity para JPA entity.
     * 
     * @param product a entidade de domínio
     * @return a entidade JPA correspondente
     * @throws NullPointerException se product for null
     */
    public ProductJpaEntity toJpaEntity(Product product) {
        Objects.requireNonNull(product, "Product cannot be null");
        
        return new ProductJpaEntity(
                product.getId().getValue(),
                product.getName(),
                product.getDescription(),
                product.getPrice().getAmount(),
                product.getPrice().getCurrency(),
                product.getStockQuantity(),
                product.getStatus(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
    
    /**
     * Converte JPA entity para domain entity.
     * 
     * @param jpaEntity a entidade JPA
     * @return a entidade de domínio correspondente
     * @throws NullPointerException se jpaEntity for null
     */
    public Product toDomainEntity(ProductJpaEntity jpaEntity) {
        Objects.requireNonNull(jpaEntity, "JPA entity cannot be null");
        
        ProductId productId = ProductId.of(jpaEntity.getId());
        Money productPrice = Money.of(jpaEntity.getPrice(), jpaEntity.getCurrency());
        
        return new Product(
                productId,
                jpaEntity.getName(),
                jpaEntity.getDescription(),
                productPrice,
                jpaEntity.getStockQuantity(),
                jpaEntity.getStatus(),
                jpaEntity.getCreatedAt(),
                jpaEntity.getUpdatedAt()
        );
    }
}