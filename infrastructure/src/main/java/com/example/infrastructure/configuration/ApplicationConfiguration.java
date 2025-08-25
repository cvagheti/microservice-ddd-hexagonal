package com.example.infrastructure.configuration;

import com.example.domain.repository.ProductRepository;
import com.example.domain.service.ProductDomainService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring Configuration for dependency injection.
 * This configuration class manages the creation and wiring of domain services
 * and other beans required by the application.
 */
@Configuration
public class ApplicationConfiguration {
    
    /**
     * Creates the ProductDomainService bean.
     * This service contains domain business logic that operates across entities.
     * 
     * @param productRepository the product repository implementation
     * @return ProductDomainService instance
     */
    @Bean
    public ProductDomainService productDomainService(ProductRepository productRepository) {
        return new ProductDomainService(productRepository);
    }
}