# Módulo de Aplicação

![Java](https://img.shields.io/badge/Java-21-orange?style=flat-square&logo=openjdk)
![Spring](https://img.shields.io/badge/Spring-Context-brightgreen?style=flat-square&logo=spring)
![CQRS](https://img.shields.io/badge/Pattern-CQRS-blue?style=flat-square)
![Architecture](https://img.shields.io/badge/Layer-Application-orange?style=flat-square)

## Visão Geral

O **Módulo de Aplicação** orquestra casos de uso de negócio e coordena entre as camadas de domínio e infraestrutura. Implementa o padrão **CQRS (Command Query Responsibility Segregation)** e define as **Portas** para a Arquitetura Hexagonal.

## 🎯 Propósito

- **Orquestração de Casos de Uso**: Coordena fluxos de trabalho de negócio complexos
- **Implementação CQRS**: Separa operações de comando (escrita) e consulta (leitura)
- **Definição de Portas**: Define portas de entrada e saída para Arquitetura Hexagonal
- **Gerenciamento de Transações**: Lida com limites transacionais
- **Transformação de DTO**: Gerencia objetos de transferência de dados para comunicação externa

## 📁 Structure

```
application/
├── src/main/java/com/example/application/
│   ├── dto/                      # Data Transfer Objects
│   │   ├── ProductDto.java
│   │   ├── CreateProductRequest.java
│   │   └── UpdateProductRequest.java
│   ├── repository/               # Outbound Ports
│   │   └── ProductRepository.java
│   ├── service/                  # Application Services (CQRS)
│   │   ├── ProductCommandService.java      # Command Service
│   │   └── ProductQueryService.java        # Query Service
│   └── usecase/                  # Inbound Ports
│       ├── ProductCommandUseCase.java      # Command Interface
│       └── ProductQueryUseCase.java        # Query Interface
└── pom.xml
```

## 🏗️ CQRS Implementation

### Command Side (Write Operations)

**Purpose**: Handle operations that modify system state

**Components**:
- **`ProductCommandUseCase`**: Interface defining write operations
- **`ProductCommandService`**: Implementation of command logic
- **Operations**: Create, Update, Add Stock, Remove Stock, Activate, Deactivate, Delete

```java
// Command Use Case Interface
public interface ProductCommandUseCase {
    Product createProduct(String name, String description, Money price, int stockQuantity);
    Product updateProduct(ProductId productId, String name, String description, Money price);
    Product addStock(ProductId productId, int quantity);
    Product removeStock(ProductId productId, int quantity);
    Product activateProduct(ProductId productId);
    Product deactivateProduct(ProductId productId);
    void deleteProduct(ProductId productId);
}
```

### Query Side (Read Operations)

**Purpose**: Handle operations that read data without modifying state

**Components**:
- **`ProductQueryUseCase`**: Interface defining read operations
- **`ProductQueryService`**: Implementation with read-only transactions
- **Operations**: Find by ID, Find All, Search, Get Statistics

```java
// Query Use Case Interface
public interface ProductQueryUseCase {
    Optional<Product> findProductById(ProductId productId);
    List<Product> findAllProducts();
    List<Product> findProductsByName(String name);
    List<Product> findActiveProducts();
    InventoryStatistics getInventoryStatistics();
}
```

### Unified Interface

**`ProductManagementUseCase`**: Combines both command and query operations for clients that need both capabilities.

## 🔌 Hexagonal Architecture Ports

### Inbound Ports (Use Cases)
Define what the application can do - the contracts for external actors:

1. **`ProductCommandUseCase`**: Write operations interface
2. **`ProductQueryUseCase`**: Read operations interface

### Outbound Ports (Repository)
Define what the application needs from external systems:

1. **`ProductRepository`**: Data persistence contract
   - Abstracts database operations
   - Implemented by infrastructure layer
   - Technology-agnostic interface

## 🔧 Application Services

### ProductCommandService
**Role**: Command operations specialist
- Handles all write operations
- Manages transactions for data modification
- Validates business rules through domain services
- Ensures data consistency

```java
@Service
@Transactional  // Write operations are transactional
public class ProductCommandService {
    
    public Product createProduct(String name, String description, Money price, int stockQuantity) {
        // 1. Create domain object
        // 2. Validate business rules
        // 3. Save through repository
        // 4. Return result
    }
}
```

### ProductQueryService
**Role**: Query operations specialist
- Handles all read operations
- Uses read-only transactions for optimization
- Focuses on data retrieval and transformation
- Calculates statistics and reports

```java
@Service
@Transactional(readOnly = true)  // Read-only optimization
public class ProductQueryService {
    
    public List<Product> findActiveProducts() {
        // Optimized read operation
        return productRepository.findByStatus(ProductStatus.ACTIVE);
    }
}
```

## 📋 Data Transfer Objects (DTOs)

### Purpose
- **Decoupling**: Separate internal domain model from external representation
- **Versioning**: Support API evolution without breaking domain
- **Validation**: Input validation and transformation
- **Security**: Control what data is exposed externally

### DTOs Available
- **`ProductDto`**: Complete product representation for API responses
- **`CreateProductRequest`**: Input for product creation
- **`UpdateProductRequest`**: Input for product updates

### Example Usage
```java
// Convert domain to DTO
ProductDto dto = ProductDto.fromDomain(product);

// Convert DTO to domain (via service)
Product product = productService.createProduct(
    request.getName(),
    request.getDescription(),
    Money.of(request.getPrice(), request.getCurrency()),
    request.getStockQuantity()
);
```

## 🧪 Testing Strategies

### Command Testing
```java
@Test
void shouldCreateProductSuccessfully() {
    // Given
    String name = "Test Product";
    Money price = Money.of(BigDecimal.TEN, "USD");
    
    // When
    Product result = commandService.createProduct(name, "Description", price, 10);
    
    // Then
    assertThat(result.getName()).isEqualTo(name);
    assertThat(result.getPrice()).isEqualTo(price);
}
```

### Query Testing
```java
@Test
void shouldFindActiveProducts() {
    // Given
    Product activeProduct = createActiveProduct();
    Product inactiveProduct = createInactiveProduct();
    
    // When
    List<Product> activeProducts = queryService.findActiveProducts();
    
    // Then
    assertThat(activeProducts)
        .hasSize(1)
        .contains(activeProduct)
        .doesNotContain(inactiveProduct);
}
```

## 🔄 Integration Points

### Domain Module
- **Uses**: Domain entities, value objects, domain services
- **Enforces**: Business rules through domain service validation
- **Coordinates**: Complex business workflows

### Infrastructure Module
- **Provides**: Repository implementations
- **Consumes**: Use case interfaces through adapters
- **Implements**: Outbound ports (repositories)

## 📏 Design Principles

### CQRS Benefits
1. **Performance**: Optimize reads and writes independently
2. **Scalability**: Scale query and command sides separately
3. **Flexibility**: Different models for reading and writing
4. **Clarity**: Clear separation of responsibilities

### Transaction Boundaries
- **Commands**: Full transactions with rollback capability
- **Queries**: Read-only transactions for performance
- **Coordination**: Transaction boundaries align with use case boundaries

### Dependency Rules
- **Inward Dependencies**: Only depends on domain layer
- **No Infrastructure**: Never depends on infrastructure layer
- **Interface Definition**: Defines interfaces for infrastructure to implement

## 🚨 Common Patterns

### Use Case Coordination
```java
public Product updateProduct(ProductId id, String name, String description, Money price) {
    // 1. Retrieve existing entity
    Product product = findExistingProduct(id);
    
    // 2. Validate business rules
    validateBusinessRules(product, name);
    
    // 3. Apply changes
    product.updateName(name);
    product.updateDescription(description);
    product.updatePrice(price);
    
    // 4. Persist changes
    return productRepository.save(product);
}
```

### Error Handling
```java
public Product findProductById(ProductId id) {
    return productRepository.findById(id)
        .orElseThrow(() -> new ProductNotFoundException(
            "Product not found with ID: " + id.getValue()
        ));
}
```

## 🔍 Architecture Validation

The application module enforces:
- **Layer Dependencies**: Only depends on domain layer
- **Interface Contracts**: Defines clear contracts for infrastructure
- **CQRS Separation**: Clear separation between commands and queries
- **Transaction Boundaries**: Proper transaction management

---

**This module orchestrates business use cases while maintaining clean separation between domain logic and infrastructure concerns.**