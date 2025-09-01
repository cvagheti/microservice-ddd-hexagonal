package com.example.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

/**
 * Testes de adesão arquitetural para validar os princípios da Arquitetura Hexagonal.
 * Verifica se as dependências seguem o fluxo correto: Infrastructure -> Application -> Domain
 */
class HexagonalArchitectureTest {

    private JavaClasses importedClasses;

    @BeforeEach
    void setUp() {
        importedClasses = new ClassFileImporter()
                .importPackages("com.example");
    }

    @Test
    @DisplayName("Deve respeitar a arquitetura em camadas hexagonal")
    void shouldRespectHexagonalLayeredArchitecture() {
        ArchRule rule = layeredArchitecture()
                .consideringAllDependencies()
                .layer("Domain").definedBy("..domain..")
                .layer("Application").definedBy("..application..")
                .layer("Infrastructure").definedBy("..infrastructure..")
                
                .whereLayer("Infrastructure").mayNotBeAccessedByAnyLayer()
                .whereLayer("Application").mayOnlyBeAccessedByLayers("Infrastructure")
                .whereLayer("Domain").mayOnlyBeAccessedByLayers("Application", "Infrastructure");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Camada de domínio não deve depender de outras camadas")
    void domainLayerShouldNotDependOnOtherLayers() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..domain..")
                .should().dependOnClassesThat()
                .resideInAnyPackage("..application..", "..infrastructure..");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Camada de aplicação não deve depender da infraestrutura")
    void applicationLayerShouldNotDependOnInfrastructure() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..application..")
                .should().dependOnClassesThat()
                .resideInAPackage("..infrastructure..");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Camada de domínio não deve usar anotações do Spring")
    void domainLayerShouldNotUseSpringAnnotations() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..domain..")
                .should().beAnnotatedWith("org.springframework.stereotype.Service")
                .orShould().beAnnotatedWith("org.springframework.stereotype.Component")
                .orShould().beAnnotatedWith("org.springframework.stereotype.Repository")
                .orShould().beAnnotatedWith("org.springframework.transaction.annotation.Transactional");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Camada de domínio não deve usar anotações JPA")
    void domainLayerShouldNotUseJpaAnnotations() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..domain..")
                .should().beAnnotatedWith("jakarta.persistence.Entity")
                .orShould().beAnnotatedWith("jakarta.persistence.Table")
                .orShould().beAnnotatedWith("jakarta.persistence.Id")
                .orShould().beAnnotatedWith("jakarta.persistence.Column");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Repositórios devem ser interfaces na camada de aplicação")
    void repositoriesShouldBeInterfacesInApplicationLayer() {
        ArchRule rule = classes()
                .that().haveNameMatching(".*Repository")
                .and().resideInAPackage("..application..")
                .should().beInterfaces();

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Use Cases devem ser interfaces na camada de aplicação")
    void useCasesShouldBeInterfacesInApplicationLayer() {
        ArchRule rule = classes()
                .that().haveNameMatching(".*UseCase")
                .and().resideInAPackage("..application..")
                .should().beInterfaces();

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Implementações de repositório devem estar na infraestrutura")
    void repositoryImplementationsShouldBeInInfrastructure() {
        ArchRule rule = classes()
                .that().haveNameMatching(".*RepositoryImpl")
                .should().resideInAPackage("..infrastructure..");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Controllers devem estar na infraestrutura")
    void controllersShouldBeInInfrastructure() {
        ArchRule rule = classes()
                .that().haveNameMatching(".*Controller")
                .should().resideInAPackage("..infrastructure..");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Adaptadores devem estar na infraestrutura")
    void adaptersShouldBeInInfrastructure() {
        ArchRule rule = classes()
                .that().haveNameMatching(".*Adapter")
                .should().resideInAPackage("..infrastructure..");

        rule.check(importedClasses);
    }
}