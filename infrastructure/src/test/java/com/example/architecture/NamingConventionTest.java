package com.example.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;

/**
 * Testes de adesão para convenções de nomenclatura e estrutura de pacotes.
 * Garante consistência no projeto seguindo padrões estabelecidos.
 */
class NamingConventionTest {

    private JavaClasses importedClasses;

    @BeforeEach
    void setUp() {
        importedClasses = new ClassFileImporter()
                .importPackages("com.example");
    }

    @Test
    @DisplayName("Classes de teste devem ter sufixo 'Test'")
    void testClassesShouldHaveTestSuffix() {
        ArchRule rule = classes()
                .that().resideInAnyPackage("..test..")
                .and().haveSimpleNameNotEndingWith("Suite")
                .should().haveSimpleNameEndingWith("Test")
                .orShould().haveSimpleNameEndingWith("Tests")
                .allowEmptyShould(true);

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Interfaces devem começar com nomes descritivos")
    void interfacesShouldHaveDescriptiveNames() {
        ArchRule rule = classes()
                .that().areInterfaces()
                .and().resideInAPackage("..application..")
                .should().haveSimpleNameNotStartingWith("I");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Classes de configuração devem ter sufixo 'Configuration'")
    void configurationClassesShouldHaveCorrectSuffix() {
        ArchRule rule = classes()
                .that().areAnnotatedWith("org.springframework.context.annotation.Configuration")
                .should().haveSimpleNameEndingWith("Configuration");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Controllers devem ter sufixo 'Controller'")
    void controllersShouldHaveCorrectSuffix() {
        ArchRule rule = classes()
                .that().areAnnotatedWith("org.springframework.web.bind.annotation.RestController")
                .or().areAnnotatedWith("org.springframework.stereotype.Controller")
                .should().haveSimpleNameEndingWith("Controller");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Serviços devem ter sufixo 'Service'")
    void servicesShouldHaveCorrectSuffix() {
        ArchRule rule = classes()
                .that().areAnnotatedWith("org.springframework.stereotype.Service")
                .should().haveSimpleNameEndingWith("Service");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Componentes devem ter sufixos apropriados")
    void componentsShouldHaveAppropriateSuffixes() {
        ArchRule rule = classes()
                .that().areAnnotatedWith("org.springframework.stereotype.Component")
                .should().haveSimpleNameEndingWith("Component")
                .orShould().haveSimpleNameEndingWith("Adapter")
                .orShould().haveSimpleNameEndingWith("Helper")
                .orShould().haveSimpleNameEndingWith("Util");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Entidades JPA devem ter sufixo 'Entity'")
    void jpaEntitiesShouldHaveCorrectSuffix() {
        ArchRule rule = classes()
                .that().areAnnotatedWith("jakarta.persistence.Entity")
                .should().haveSimpleNameEndingWith("Entity")
                .orShould().haveSimpleNameEndingWith("JpaEntity");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Repositórios JPA devem ter sufixo 'Repository'")
    void jpaRepositoriesShouldHaveCorrectSuffix() {
        ArchRule rule = classes()
                .that().areAssignableTo("org.springframework.data.jpa.repository.JpaRepository")
                .should().haveSimpleNameEndingWith("Repository")
                .orShould().haveSimpleNameEndingWith("JpaRepository");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Exceções devem ter sufixo 'Exception'")
    void exceptionsShouldHaveCorrectSuffix() {
        ArchRule rule = classes()
                .that().areAssignableTo(Exception.class)
                .should().haveSimpleNameEndingWith("Exception")
                .allowEmptyShould(true);

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("DTOs devem estar no pacote correto")
    void dtosShouldBeInCorrectPackage() {
        ArchRule rule = classes()
                .that().haveSimpleNameEndingWith("Dto")
                .or().haveSimpleNameEndingWith("Request")
                .or().haveSimpleNameEndingWith("Response")
                .should().resideInAPackage("..dto..");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Value Objects devem ter nomes apropriados")
    void valueObjectsShouldHaveAppropriateNames() {
        ArchRule rule = classes()
                .that().resideInAPackage("..domain.model..")
                .and().haveSimpleNameContaining("Id")
                .or().haveSimpleNameContaining("Money")
                .or().haveSimpleNameContaining("Status")
                .or().haveSimpleNameContaining("Address")
                .or().haveSimpleNameContaining("Email")
                .should().notBeInterfaces();

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Pacotes devem seguir convenção de nomenclatura")
    void packagesShouldFollowNamingConvention() {
        ArchRule rule = classes()
                .that().doNotBelongToAnyOf(Object.class)
                .should().resideInAnyPackage(
                        "com.example.domain.model..",
                        "com.example.domain.service..",
                        "com.example.application.dto..",
                        "com.example.application.repository..",
                        "com.example.application.service..",
                        "com.example.application.usecase..",
                        "com.example.infrastructure.adapter..",
                        "com.example.infrastructure.configuration..",
                        "com.example.infrastructure.persistence..",
                        "com.example.infrastructure..",
                        "com.example.architecture.." // Para testes
                )
                .allowEmptyShould(true);

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Classes não devem ter nomes genéricos")
    void classesShouldNotHaveGenericNames() {
        ArchRule rule = noClasses()
                .should().haveSimpleName("Manager")
                .orShould().haveSimpleName("Helper")
                .orShould().haveSimpleName("Util")
                .orShould().haveSimpleName("Handler")
                .orShould().haveSimpleName("Processor")
                .allowEmptyShould(true);

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Métodos públicos devem seguir convenção camelCase")
    void publicMethodsShouldFollowCamelCase() {
        // Verificação simplificada - métodos não devem ter underscore
        ArchRule rule = methods()
                .that().arePublic()
                .and().doNotHaveName("main")
                .should().haveNameNotContaining("_")
                .allowEmptyShould(true);

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Constantes devem estar em UPPER_SNAKE_CASE")
    void constantsShouldBeInUpperSnakeCase() {
        ArchRule rule = fields()
                .that().areStatic()
                .and().areFinal()
                .and().arePublic()
                .should().haveNameNotContaining("[a-z]")
                .allowEmptyShould(true);

        rule.check(importedClasses);
    }
}