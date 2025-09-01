# Módulo de Domínio

![Java](https://img.shields.io/badge/Java-21-orange?style=flat-square&logo=openjdk)
![DDD](https://img.shields.io/badge/DDD-Domain%20Driven%20Design-yellow?style=flat-square)
![Architecture](https://img.shields.io/badge/Layer-Domain-green?style=flat-square)

## Visão Geral

O **Módulo de Domínio** é o coração da aplicação, contendo a lógica de negócio pura e as regras. Este módulo segue os princípios de Domain-Driven Design (DDD) e tem **zero dependências externas** exceto pelas bibliotecas padrão do Java e Jakarta Validation.

## 🎯 Propósito

- **Lógica de Negócio**: Contém todas as regras centrais de negócio e conhecimento do domínio
- **Modelo de Domínio**: Define entidades, value objects e serviços de domínio
- **Java Puro**: Sem dependências de frameworks, garantindo testabilidade e portabilidade
- **Independência Tecnológica**: Pode ser usado com qualquer framework ou tecnologia de persistência

## 📁 Estrutura

```
domain/
├── src/main/java/com/example/domain/
│   ├── model/                    # Modelo de Domínio
│   │   ├── Product.java          # Agregado Raiz
│   │   ├── ProductId.java        # Value Object
│   │   ├── ProductStatus.java    # Value Object (Enum)
│   │   └── Money.java            # Value Object
│   └── service/                  # Serviços de Domínio
│       └── ProductDomainService.java
└── pom.xml
```

## 🏗️ Conceitos Domain-Driven Design

### 1. Agregado Raiz
- **`Product`**: Entidade principal que controla o acesso ao agregado produto
  - Encapsula regras de negócio para o ciclo de vida do produto
  - Valida operações de estoque
  - Controla transições de estado (ACTIVE ↔ INACTIVE ↔ DISCONTINUED)
  - Garante consistência de dados dentro dos limites do agregado

### 2. Value Objects
- **`ProductId`**: Identificador único para produtos
  - Imutável e type-safe
  - Geração automática via UUID
  - Validação para valores não-nulos/vazios

- **`Money`**: Representa valores monetários
  - Encapsula valor e moeda
  - Validação para valores positivos
  - Comportamento rico do domínio

- **`ProductStatus`**: Estados do ciclo de vida do produto
  - Enum com definições claras de estado
  - Valores descritivos para exibição na UI

### 3. Serviços de Domínio
- **`ProductDomainService`**: Coordena lógica de negócio que não pertence a uma única entidade
  - Validação de unicidade do nome do produto
  - Regras de negócio entre entidades
  - Cálculo de estatísticas de inventário
  - Funções puras sem efeitos colaterais

## 🔧 Funcionalidades Principais

### Regras de Negócio Implementadas
- **Gerenciamento de Estoque**: Adicionar/remover estoque com validação
- **Ciclo de Vida do Produto**: Ativar, desativar, descontinuar produtos
- **Validação de Dados**: Unicidade de nomes, valores positivos, campos obrigatórios
- **Controle de Inventário**: Estoque não pode ficar negativo
- **Transições de Estado**: Transições válidas entre estados do produto

### Invariantes do Domínio
- Produtos devem ter nomes válidos (não vazios, únicos)
- Preços devem ser valores positivos
- Quantidades de estoque não podem ser negativas
- IDs de produto devem ser únicos e UUIDs válidos

## 📋 Exemplos de Uso

### Criando um Produto
``java
// Criar value objects
ProductId id = ProductId.generate();
Money price = Money.of(new BigDecimal("99.99"), "USD");

// Criar produto
Product product = Product.create(id, "iPhone 15", "Latest iPhone", price, 100);
```

### Gerenciamento de Estoque
``java
// Adicionar estoque
product.addStock(50);

// Remover estoque com validação
product.removeStock(10); // Lança exceção se estoque insuficiente
```

### Gerenciamento de Status
``java
// Ativar produto
product.activate();

// Desativar produto
product.deactivate();
```

### Uso do Serviço de Domínio
``java
ProductDomainService domainService = new ProductDomainService();

// Validar unicidade
boolean isUnique = domainService.isProductNameUnique(
    "Novo Produto", 
    existingProducts, 
    excludeId
);

// Calcular estatísticas
var stats = domainService.calculateInventoryStatistics(products);
```

## 🧪 Testes

A camada de domínio é projetada para facilitar testes unitários:

``java
@Test
void shouldAddStockCorrectly() {
    // Given
    Product product = Product.create(
        ProductId.generate(),
        "Test Product",
        "Description",
        Money.of(BigDecimal.TEN, "USD"),
        10
    );
    
    // When
    product.addStock(5);
    
    // Then
    assertThat(product.getStockQuantity()).isEqualTo(15);
}
```

## 🚫 Dependências

**O que este módulo NÃO depende:**
- Spring Framework
- JPA/Hibernate
- Drivers de banco de dados
- Frameworks web
- Bibliotecas externas (exceto Jakarta Validation)

**O que este módulo PODE usar:**
- Biblioteca padrão Java 21
- Anotações Jakarta Validation
- Frameworks de teste Java puros (JUnit, AssertJ)

## 🔄 Integração com Outros Módulos

### Módulo de Aplicação
- Define interfaces de repositório (`ProductRepository`)
- Consumido pelos serviços de aplicação
- Fornece serviços de domínio para lógica de negócio

### Módulo de Infraestrutura
- Entidades de domínio mapeadas para entidades JPA
- Regras de negócio aplicadas na camada de persistência
- Nenhuma dependência direta - apenas através de interfaces

## 📝 Diretrizes de Desenvolvimento

1. **Mantenha Puro**: Sem dependências de frameworks
2. **Modelo de Domínio Rico**: Encapsule comportamento, não apenas dados
3. **Value Objects Imutáveis**: Prefira imutabilidade para value objects
4. **Limites Claros**: Agregados raiz controlam acesso aos seus dados
5. **Linguagem do Domínio**: Use linguagem ubíqua do domínio de negócio
6. **Validação**: Valide invariantes dentro das entidades
7. **Sem Efeitos Colaterais**: Serviços de domínio devem ser funções puras

## 🔍 Validação Arquitetural

O módulo de domínio inclui restrições arquiteturais:
- Nenhuma dependência das camadas de aplicação ou infraestrutura
- Apenas Java puro e dependências de validação permitidas
- Lógica de negócio deve permanecer na camada de domínio

---

**Este módulo representa o núcleo do domínio de negócio e deve permanecer estável e independente de escolhas tecnológicas.**