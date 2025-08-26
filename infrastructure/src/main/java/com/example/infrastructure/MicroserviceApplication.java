package com.example.infrastructure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Spring Boot Main Application.
 * This is the entry point for the microservice application.
 */
@SpringBootApplication(scanBasePackages = "com.example")
@EntityScan(basePackages = "com.example.infrastructure.persistence")
@EnableJpaRepositories(basePackages = "com.example.infrastructure.persistence")
@EnableTransactionManagement
public class MicroserviceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(MicroserviceApplication.class, args);
    }
}