package com.example.infrastructure.configuration;

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
     * This service contains pure domain business logic with no external dependencies.
     * Following DDD principles, it operates on data provided as parameters.
     * 
     * @return ProductDomainService instance
     */
    @Bean
    public ProductDomainService productDomainService() {
        return new ProductDomainService();
    }
}