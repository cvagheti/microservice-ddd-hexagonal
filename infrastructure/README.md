# Módulo de Infraestrutura

![Java](https://img.shields.io/badge/Java-21-orange?style=flat-square&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.1.5-brightgreen?style=flat-square&logo=spring-boot)
![JPA](https://img.shields.io/badge/JPA-Hibernate-red?style=flat-square)
![Architecture](https://img.shields.io/badge/Layer-Infrastructure-red?style=flat-square)

## Visão Geral

O **Módulo de Infraestrutura** contém todos os detalhes de implementação técnica e integrações externas. Este módulo implementa os **Adaptadores** na Arquitetura Hexagonal e fornece implementações concretas para as portas definidas na camada de aplicação.

## 🎯 Propósito

- **Implementação Técnica**: Contém todo o código específico de frameworks
- **Integração Externa**: Banco de dados, APIs REST, gerenciamento de configuração
- **Implementação de Adaptadores**: Adaptadores primários e secundários para arquitetura hexagonal
- **Injeção de Dependência**: Configuração do Spring Boot e gerenciamento de beans
- **Bootstrap da Aplicação**: Ponto de entrada principal da aplicação

## 📁 Structure

```
infrastructure/
├── src/main/
│   ├── java/com/example/infrastructure/
│   │   ├── MicroserviceApplication.java      # Spring Boot Main Class
│   │   ├── adapter/
│   │   │   └── web/                          # Primary Adapters (Inbound)
│   │   │       ├── ProductController.java        # REST Controller
│   │   │       ├── ProductCommandAdapter.java    # Command Adapter
│   │   │       └── ProductQueryAdapter.java      # Query Adapter
│   │   ├── configuration/
│   │   │   └── ApplicationConfiguration.java     # Spring Configuration
│   │   └── persistence/                      # Secondary Adapters (Outbound)
│   │       ├── ProductJpaEntity.java             # JPA Entity
│   │       ├── ProductJpaRepository.java         # Spring Data Repository
│   │       └── ProductRepositoryImpl.java        # Repository Implementation
│   └── resources/
│       ├── application.yml                   # Development Configuration
│       ├── application-prod.yml              # Production Configuration
│       └── data.sql                         # Sample Data
└── pom.xml
```

## 🏗️ Hexagonal Architecture Adapters

### Primary Adapters (Inbound)
Handle external requests and drive the application:

#### 1. REST Controller
**`ProductController`**: Main REST API endpoint
- Handles HTTP requests/responses
- Delegates to appropriate adapters
- Manages request/response transformation
- Implements error handling

#### 2. Use Case Adapters
**`ProductCommandAdapter`**: Command operations specialist
- Implements `ProductCommandUseCase`
- Delegates to `ProductCommandService`
- Optimized for write operations

**`ProductQueryAdapter`**: Query operations specialist
- Implements `ProductQueryUseCase`
- Delegates to `ProductQueryService`
- Optimized for read operations

### Secondary Adapters (Outbound)
Implement external services and data persistence:

#### 1. Persistence Adapter
**`ProductRepositoryImpl`**: Repository interface implementation
- Implements `ProductRepository` from application layer
- Bridges domain and JPA entities
- Handles data transformation
- Manages database operations

#### 2. JPA Components
**`ProductJpaEntity`**: Database entity mapping
- Maps domain model to database tables
- Handles ORM annotations
- Conversion to/from domain objects

**`ProductJpaRepository`**: Spring Data repository
- Extends `JpaRepository<ProductJpaEntity, String>`
- Custom query methods
- Database-specific operations

## 🔧 Technical Implementation

### REST API Endpoints

```java
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    
    @GetMapping
    public List<ProductDto> getAllProducts() {
        // Delegates to Query Adapter
    }
    
    @PostMapping
    public ProductDto createProduct(@RequestBody CreateProductRequest request) {
        // Delegates to Command Adapter
    }
    
    @GetMapping("/{id}")
    public ProductDto getProduct(@PathVariable String id) {
        // Delegates to Query Adapter
    }
}
```

### Adapter Pattern Implementation

```java
@Component
public class ProductCommandAdapter implements ProductCommandUseCase {
    
    private final ProductCommandService commandService;
    
    @Override
    public Product createProduct(String name, String description, Money price, int stockQuantity) {
        return commandService.createProduct(name, description, price, stockQuantity);
    }
    
    // ... other command methods
}
```

### Repository Implementation

```java
@Repository
public class ProductRepositoryImpl implements ProductRepository {
    
    private final ProductJpaRepository jpaRepository;
    
    @Override
    public Product save(Product product) {
        ProductJpaEntity entity = ProductJpaEntity.fromDomain(product);
        ProductJpaEntity saved = jpaRepository.save(entity);
        return saved.toDomain();
    }
    
    @Override
    public Optional<Product> findById(ProductId id) {
        return jpaRepository.findById(id.getValue())
            .map(ProductJpaEntity::toDomain);
    }
}
```

## 🗄️ Database Configuration

### Development Environment (H2)
```yaml
spring:
  datasource:
    url: jdbc:h2:mem:testdb
    driver-class-name: org.h2.Driver
    username: sa
    password: password
  
  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: true
  
  h2:
    console:
      enabled: true
```

### Production Environment (PostgreSQL)
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/productdb
    username: ${DB_USERNAME:productuser}
    password: ${DB_PASSWORD:productpass}
    driver-class-name: org.postgresql.Driver

  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false
```

## 🔌 Spring Boot Configuration

### Application Configuration
```java
@Configuration
@EnableJpaRepositories(basePackages = "com.example.infrastructure.persistence")
@ComponentScan(basePackages = {
    "com.example.application",
    "com.example.infrastructure"
})
public class ApplicationConfiguration {
    
    @Bean
    public ProductDomainService productDomainService() {
        return new ProductDomainService();
    }
}
```

### Main Application Class
```java
@SpringBootApplication
public class MicroserviceApplication {
    public static void main(String[] args) {
        SpringApplication.run(MicroserviceApplication.class, args);
    }
}
```

## 🚀 Running the Application

### Development Mode
```bash
# From project root
mvn spring-boot:run -pl infrastructure

# With specific profile
mvn spring-boot:run -pl infrastructure -Dspring.profiles.active=dev
```

### Production Mode
```bash
mvn spring-boot:run -pl infrastructure -Dspring.profiles.active=prod
```

### Building and Packaging
```bash
# Build all modules
mvn clean compile

# Run tests
mvn test

# Package application
mvn clean package

# Run packaged JAR
java -jar infrastructure/target/infrastructure-1.0.0-SNAPSHOT.jar
```

## 📊 Monitoring and Operations

### Actuator Endpoints
- **Health Check**: `/actuator/health`
- **Application Info**: `/actuator/info`
- **Metrics**: `/actuator/metrics`

### H2 Console (Development)
- **URL**: `http://localhost:8080/h2-console`
- **JDBC URL**: `jdbc:h2:mem:testdb`
- **Username**: `sa`
- **Password**: `password`

### API Documentation
Base URL: `http://localhost:8080/api/v1`

Example endpoints:
- `GET /products` - List all products
- `GET /products/{id}` - Get product by ID
- `POST /products` - Create new product
- `PUT /products/{id}` - Update product
- `DELETE /products/{id}` - Delete product

## 🧪 Testing Infrastructure

### Integration Tests
```java
@SpringBootTest
@TestPropertySource(properties = {
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.datasource.url=jdbc:h2:mem:test"
})
class ProductControllerIntegrationTest {
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Test
    void shouldCreateProductViaAPI() {
        // Test complete integration flow
    }
}
```

### Repository Tests
```java
@DataJpaTest
class ProductRepositoryImplTest {
    
    @Autowired
    private TestEntityManager entityManager;
    
    @Autowired
    private ProductJpaRepository jpaRepository;
    
    @Test
    void shouldSaveAndFindProduct() {
        // Test repository implementation
    }
}
```

## 🔧 Configuration Management

### Environment Variables
- **`DB_USERNAME`**: Database username (production)
- **`DB_PASSWORD`**: Database password (production)
- **`SERVER_PORT`**: Application port (default: 8080)

### Profiles
- **`default`**: Development with H2 database
- **`prod`**: Production with PostgreSQL
- **`test`**: Testing with in-memory database

## 📈 Performance Considerations

### Database Optimization
- Connection pooling configured via Spring Boot
- JPA query optimization with `show-sql` in development
- Proper indexing in production database schema

### Caching Strategy
- Query result caching can be added at service level
- HTTP response caching via Spring cache abstraction
- Database query plan caching through JPA

## 🔒 Security Configuration

### Input Validation
- Jakarta Validation annotations on DTOs
- Request body validation in controllers
- Path variable validation

### Error Handling
- Global exception handler for consistent error responses
- Proper HTTP status codes
- Security-aware error messages (no sensitive data exposure)

## 🔄 Integration Points

### Application Module
- **Implements**: All use case interfaces (ports)
- **Uses**: Application services for business logic
- **Provides**: Repository implementations

### Domain Module
- **Maps**: Domain entities to JPA entities
- **Preserves**: Domain model integrity
- **Enforces**: Business rules at persistence layer

## 📏 Architecture Compliance

### Dependency Rules
- **Can depend on**: Application and Domain modules
- **Provides implementations for**: Application layer interfaces
- **Isolates**: Technical details from business logic

### Framework Integration
- **Spring Boot**: Application bootstrap and dependency injection
- **Spring Data JPA**: Repository pattern implementation
- **Spring Web**: REST API endpoints
- **Spring Actuator**: Monitoring and health checks

---

**This module handles all technical concerns while keeping business logic isolated in the domain and application layers.**