package com.example.infrastructure.persistence.mapper;

import com.example.domain.model.Product;
import com.example.domain.model.ProductId;
import com.example.domain.model.ProductStatus;
import com.example.domain.model.Money;
import com.example.infrastructure.persistence.ProductJpaEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

/**
 * Testes unitários para o ProductMapper.
 * Valida as conversões entre entidades de domínio e JPA.
 */
class ProductMapperTest {

    private ProductMapper mapper;
    private Product sampleProduct;
    private ProductJpaEntity sampleJpaEntity;

    @BeforeEach
    void setUp() {
        mapper = new ProductMapper();
        
        // Produto de domínio de exemplo
        ProductId productId = ProductId.of("test-id-123");
        Money price = Money.of(new BigDecimal("29.99"), "USD");
        LocalDateTime now = LocalDateTime.now();
        
        sampleProduct = new Product(
                productId,
                "Test Product",
                "Test Description",
                price,
                10,
                ProductStatus.ACTIVE,
                now,
                now
        );
        
        // Entidade JPA de exemplo
        sampleJpaEntity = new ProductJpaEntity(
                "test-id-123",
                "Test Product",
                "Test Description",
                new BigDecimal("29.99"),
                "USD",
                10,
                ProductStatus.ACTIVE,
                now,
                now
        );
    }

    @Test
    @DisplayName("Deve converter Product (domain) para ProductJpaEntity (infrastructure)")
    void shouldConvertDomainEntityToJpaEntity() {
        // When
        ProductJpaEntity result = mapper.toJpaEntity(sampleProduct);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("test-id-123");
        assertThat(result.getName()).isEqualTo("Test Product");
        assertThat(result.getDescription()).isEqualTo("Test Description");
        assertThat(result.getPrice()).isEqualTo(new BigDecimal("29.99"));
        assertThat(result.getCurrency()).isEqualTo("USD");
        assertThat(result.getStockQuantity()).isEqualTo(10);
        assertThat(result.getStatus()).isEqualTo(ProductStatus.ACTIVE);
        assertThat(result.getCreatedAt()).isEqualTo(sampleProduct.getCreatedAt());
        assertThat(result.getUpdatedAt()).isEqualTo(sampleProduct.getUpdatedAt());
    }

    @Test
    @DisplayName("Deve converter ProductJpaEntity (infrastructure) para Product (domain)")
    void shouldConvertJpaEntityToDomainEntity() {
        // When
        Product result = mapper.toDomainEntity(sampleJpaEntity);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId().getValue()).isEqualTo("test-id-123");
        assertThat(result.getName()).isEqualTo("Test Product");
        assertThat(result.getDescription()).isEqualTo("Test Description");
        assertThat(result.getPrice().getAmount()).isEqualTo(new BigDecimal("29.99"));
        assertThat(result.getPrice().getCurrency()).isEqualTo("USD");
        assertThat(result.getStockQuantity()).isEqualTo(10);
        assertThat(result.getStatus()).isEqualTo(ProductStatus.ACTIVE);
        assertThat(result.getCreatedAt()).isEqualTo(sampleJpaEntity.getCreatedAt());
        assertThat(result.getUpdatedAt()).isEqualTo(sampleJpaEntity.getUpdatedAt());
    }

    @Test
    @DisplayName("Deve manter consistência na conversão ida e volta (domain -> jpa -> domain)")
    void shouldMaintainConsistencyInRoundTripConversion() {
        // When
        ProductJpaEntity jpaEntity = mapper.toJpaEntity(sampleProduct);
        Product reconstructedProduct = mapper.toDomainEntity(jpaEntity);
        
        // Then
        assertThat(reconstructedProduct.getId()).isEqualTo(sampleProduct.getId());
        assertThat(reconstructedProduct.getName()).isEqualTo(sampleProduct.getName());
        assertThat(reconstructedProduct.getDescription()).isEqualTo(sampleProduct.getDescription());
        assertThat(reconstructedProduct.getPrice()).isEqualTo(sampleProduct.getPrice());
        assertThat(reconstructedProduct.getStockQuantity()).isEqualTo(sampleProduct.getStockQuantity());
        assertThat(reconstructedProduct.getStatus()).isEqualTo(sampleProduct.getStatus());
        assertThat(reconstructedProduct.getCreatedAt()).isEqualTo(sampleProduct.getCreatedAt());
        assertThat(reconstructedProduct.getUpdatedAt()).isEqualTo(sampleProduct.getUpdatedAt());
    }

    @Test
    @DisplayName("Deve lançar exceção quando Product for null")
    void shouldThrowExceptionWhenProductIsNull() {
        // When & Then
        assertThatThrownBy(() -> mapper.toJpaEntity(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Product cannot be null");
    }

    @Test
    @DisplayName("Deve lançar exceção quando ProductJpaEntity for null")
    void shouldThrowExceptionWhenJpaEntityIsNull() {
        // When & Then
        assertThatThrownBy(() -> mapper.toDomainEntity(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("JPA entity cannot be null");
    }

    @Test
    @DisplayName("Deve converter corretamente diferentes status de produto")
    void shouldConvertDifferentProductStatusesCorrectly() {
        // Given
        ProductStatus[] statuses = {ProductStatus.ACTIVE, ProductStatus.INACTIVE, ProductStatus.DISCONTINUED};
        
        for (ProductStatus status : statuses) {
            ProductId productId = ProductId.of("test-id-" + status);
            Money price = Money.of(new BigDecimal("19.99"), "EUR");
            LocalDateTime now = LocalDateTime.now();
            
            Product product = new Product(
                    productId,
                    "Test Product " + status,
                    "Test Description",
                    price,
                    5,
                    status,
                    now,
                    now
            );
            
            // When
            ProductJpaEntity jpaEntity = mapper.toJpaEntity(product);
            Product reconstructed = mapper.toDomainEntity(jpaEntity);
            
            // Then
            assertThat(reconstructed.getStatus()).isEqualTo(status);
        }
    }

    @Test
    @DisplayName("Deve converter corretamente diferentes moedas")
    void shouldConvertDifferentCurrenciesCorrectly() {
        // Given
        String[] currencies = {"USD", "EUR", "BRL", "GBP"};
        
        for (String currency : currencies) {
            ProductId productId = ProductId.of("test-id-" + currency);
            Money price = Money.of(new BigDecimal("99.99"), currency);
            LocalDateTime now = LocalDateTime.now();
            
            Product product = new Product(
                    productId,
                    "Test Product " + currency,
                    "Test Description",
                    price,
                    15,
                    ProductStatus.ACTIVE,
                    now,
                    now
            );
            
            // When
            ProductJpaEntity jpaEntity = mapper.toJpaEntity(product);
            Product reconstructed = mapper.toDomainEntity(jpaEntity);
            
            // Then
            assertThat(reconstructed.getPrice().getCurrency()).isEqualTo(currency);
            assertThat(jpaEntity.getCurrency()).isEqualTo(currency);
        }
    }
}