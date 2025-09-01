# Testes de Adesão Arquitetural

Este diretório contém testes automatizados que validam a aderência do código aos princípios arquiteturais estabelecidos no projeto. Os testes utilizam a biblioteca [ArchUnit](https://www.archunit.org/) **versão 1.4.1** (mais recente) para verificar regras arquiteturais de forma automatizada.

## 📁 Estrutura dos Testes

### 1. `HexagonalArchitectureTest.java`
Valida os princípios da **Arquitetura Hexagonal (Ports & Adapters)**:

- ✅ Fluxo de dependências: Infrastructure → Application → Domain
- ✅ Isolamento da camada de domínio (sem dependências externas)
- ✅ Camada de aplicação não depende da infraestrutura
- ✅ Domain não usa anotações Spring ou JPA
- ✅ Repositórios são interfaces na camada de aplicação
- ✅ Use Cases são interfaces na camada de aplicação
- ✅ Implementações estão na infraestrutura

### 2. `DomainDrivenDesignTest.java`
Valida os princípios de **Domain-Driven Design (DDD)**:

- ✅ Value Objects são imutáveis (sem setters)
- ✅ Entidades têm identidade única
- ✅ Serviços de domínio são stateless
- ✅ Value Objects implementam equals() e hashCode()
- ✅ Modelos não expõem campos diretamente
- ✅ Enums de domínio estão no pacote model
- ✅ Agregados têm construtores que validam invariantes

### 3. `CqrsArchitectureTest.java`
Valida os princípios do padrão **CQRS (Command Query Responsibility Segregation)**:

- ✅ Separação clara entre Commands e Queries
- ✅ Nomenclatura correta (CommandUseCase, QueryUseCase, etc.)
- ✅ Serviços de comando são transacionais
- ✅ Serviços de query têm transações read-only
- ✅ DTOs têm sufixos apropriados (Request, Dto, Response)
- ✅ Commands não retornam listas
- ✅ Adaptadores estão na infraestrutura

### 4. `NamingConventionTest.java`
Valida **convenções de nomenclatura e estrutura**:

- ✅ Classes de teste terminam com "Test"
- ✅ Interfaces têm nomes descritivos
- ✅ Configurações terminam com "Configuration"
- ✅ Controllers terminam com "Controller"
- ✅ Serviços terminam com "Service"
- ✅ Entidades JPA terminam com "Entity"
- ✅ Métodos seguem camelCase
- ✅ Constantes em UPPER_SNAKE_CASE
- ✅ Pacotes seguem estrutura definida

### 5. `ArchitectureTestSuite.java`
Suite que executa todos os testes arquiteturais em conjunto.

## 🛠️ Configuração Técnica

### Dependências Necessárias:
```xml
<!-- ArchUnit para testes de arquitetura (versão mais recente) -->
<dependency>
    <groupId>com.tngtech.archunit</groupId>
    <artifactId>archunit</artifactId>
    <version>1.4.1</version>
    <scope>test</scope>
</dependency>

<!-- JUnit Platform Suite para suites de teste -->
<dependency>
    <groupId>org.junit.platform</groupId>
    <artifactId>junit-platform-suite</artifactId>
    <scope>test</scope>
</dependency>
```

### Arquivo de Configuração:
- 📁 `src/test/resources/archunit.properties` - Configuração global do ArchUnit

## 🚀 Como Executar

### Executar todos os testes arquiteturais:
```bash
mvn test -Dtest="*ArchitectureTest,*Test" -pl infrastructure
```

### Executar apenas testes de arquitetura:
```bash
mvn test -Dtest="*ArchitectureTest" -pl infrastructure
```

### Executar testes específicos:
```bash
# Apenas testes de Arquitetura Hexagonal
mvn test -Dtest=HexagonalArchitectureTest

# Apenas testes de DDD
mvn test -Dtest=DomainDrivenDesignTest

# Apenas testes de CQRS
mvn test -Dtest=CqrsArchitectureTest

# Apenas testes de nomenclatura
mvn test -Dtest=NamingConventionTest
```

### Executar com relatório detalhado:
```bash
mvn test -Dtest=ArchitectureTestSuite -Dmaven.surefire.debug=true
```

## 📊 Benefícios dos Testes de Adesão

### 1. **Manutenção da Qualidade Arquitetural**
- Previne violações arquiteturais durante o desenvolvimento
- Detecta regressões arquiteturais automaticamente
- Garante consistência entre diferentes desenvolvedores

### 2. **Documentação Viva**
- Os testes servem como documentação executável das regras arquiteturais
- Novos desenvolvedores podem entender a arquitetura através dos testes
- Facilita onboarding e transferência de conhecimento

### 3. **Refatoração Segura**
- Permite refatorações com confiança
- Detecta quebras arquiteturais imediatamente
- Mantém a integridade do design durante mudanças

### 4. **CI/CD Integration**
- Integração automática com pipelines de CI/CD
- Falha fast quando regras são violadas
- Previne deploy de código que viola arquitetura

## 🔧 Configuração no Pipeline

### GitHub Actions exemplo:
```yaml
- name: Run Architecture Tests
  run: mvn test -Dtest=ArchitectureTestSuite
```

### Jenkins exemplo:
```groovy
stage('Architecture Tests') {
    steps {
        sh 'mvn test -Dtest=ArchitectureTestSuite'
    }
    post {
        always {
            publishTestResults testResultsPattern: 'target/surefire-reports/*.xml'
        }
    }
}
```

## 📝 Adicionando Novas Regras

Para adicionar novas regras arquiteturais:

1. **Identifique o arquivo de teste apropriado** (ou crie um novo)
2. **Adicione o método de teste** com anotação `@Test`
3. **Use a DSL do ArchUnit** para definir a regra
4. **Adicione à suite** se necessário

### Exemplo de nova regra:
```java
@Test
@DisplayName("Services não devem depender de Controllers")
void servicesShouldNotDependOnControllers() {
    ArchRule rule = noClasses()
            .that().resideInAPackage("..service..")
            .should().dependOnClassesThat()
            .resideInAPackage("..controller..");
    
    rule.check(importedClasses);
}
```

## 🎯 Objetivos Arquiteturais

Esses testes garantem que o projeto mantenha:

- **Separação de Responsabilidades**: Cada camada tem sua responsabilidade bem definida
- **Inversão de Dependência**: Dependências apontam para abstrações, não implementações
- **Princípios SOLID**: Código segue princípios de design orientado a objetos
- **Clean Architecture**: Arquitetura limpa e testável
- **Domain Purity**: Domínio livre de dependências técnicas

## 🚀 Recursos da Versão 1.4.1

A versão mais recente do ArchUnit oferece:

- **🏎️ Performance Melhorada**: Análise mais rápida de grandes bases de código
- **🔧 Novas Configurações**: Mais opções de configuração via `archunit.properties`
- **☕ Suporte Java Moderno**: Melhor compatibilidade com Java 21+
- **📊 Métricas Avançadas**: Novas métricas de arquitetura (Lakos, Visibility)
- **🎯 Regras Mais Precisas**: Predicados mais específicos e eficientes
- **🛡️ Freezing Rules**: Funcionalidade aprimorada para regras congeladas
- **📈 PlantUML Integration**: Melhor integração com diagramas PlantUML

## 📚 Recursos Adicionais

- [ArchUnit User Guide](https://www.archunit.org/userguide/html/000_Index.html)
- [ArchUnit Examples](https://github.com/TNG/ArchUnit-Examples)
- [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [Hexagonal Architecture](https://alistair.cockburn.us/hexagonal-architecture/)