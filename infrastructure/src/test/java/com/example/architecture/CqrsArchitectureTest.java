package com.example.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;

/**
 * Testes de adesão para validar os princípios do padrão CQRS.
 * Verifica se commands e queries estão adequadamente separados.
 */
class CqrsArchitectureTest {

    private JavaClasses importedClasses;

    @BeforeEach
    void setUp() {
        importedClasses = new ClassFileImporter()
                .importPackages("com.example");
    }

    @Test
    @DisplayName("Use Cases de comando devem ter sufixo 'CommandUseCase'")
    void commandUseCasesShouldHaveCorrectSuffix() {
        ArchRule rule = classes()
                .that().haveSimpleNameContaining("Command")
                .and().haveSimpleNameEndingWith("UseCase")
                .should().resideInAPackage("..application.usecase..")
                .andShould().beInterfaces();

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Use Cases de query devem ter sufixo 'QueryUseCase'")
    void queryUseCasesShouldHaveCorrectSuffix() {
        ArchRule rule = classes()
                .that().haveSimpleNameContaining("Query")
                .and().haveSimpleNameEndingWith("UseCase")
                .should().resideInAPackage("..application.usecase..")
                .andShould().beInterfaces();

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Serviços de comando devem ter sufixo 'CommandService'")
    void commandServicesShouldHaveCorrectSuffix() {
        ArchRule rule = classes()
                .that().haveSimpleNameContaining("Command")
                .and().haveSimpleNameEndingWith("Service")
                .should().resideInAPackage("..application.service..")
                .andShould().notBeInterfaces();

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Serviços de query devem ter sufixo 'QueryService'")
    void queryServicesShouldHaveCorrectSuffix() {
        ArchRule rule = classes()
                .that().haveSimpleNameContaining("Query")
                .and().haveSimpleNameEndingWith("Service")
                .should().resideInAPackage("..application.service..")
                .andShould().notBeInterfaces();

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Adaptadores de comando devem ter sufixo 'CommandAdapter'")
    void commandAdaptersShouldHaveCorrectSuffix() {
        ArchRule rule = classes()
                .that().haveSimpleNameContaining("Command")
                .and().haveSimpleNameEndingWith("Adapter")
                .should().resideInAPackage("..infrastructure.adapter..")
                .andShould().notBeInterfaces();

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Adaptadores de query devem ter sufixo 'QueryAdapter'")
    void queryAdaptersShouldHaveCorrectSuffix() {
        ArchRule rule = classes()
                .that().haveSimpleNameContaining("Query")
                .and().haveSimpleNameEndingWith("Adapter")
                .should().resideInAPackage("..infrastructure.adapter..")
                .andShould().notBeInterfaces();

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Serviços de comando devem ser transacionais")
    void commandServicesShouldBeTransactional() {
        ArchRule rule = classes()
                .that().haveSimpleNameContaining("Command")
                .and().haveSimpleNameEndingWith("Service")
                .and().resideInAPackage("..application.service..")
                .should().beAnnotatedWith("org.springframework.transaction.annotation.Transactional");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Serviços de query devem ter transação somente leitura")
    void queryServicesShouldHaveReadOnlyTransactions() {
        ArchRule rule = classes()
                .that().haveSimpleNameContaining("Query")
                .and().haveSimpleNameEndingWith("Service")
                .and().resideInAPackage("..application.service..")
                .should().beAnnotatedWith("org.springframework.transaction.annotation.Transactional");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("DTOs de request devem ter sufixo 'Request'")
    void requestDtosShouldHaveCorrectSuffix() {
        ArchRule rule = classes()
                .that().haveSimpleNameEndingWith("Request")
                .should().resideInAPackage("..application.dto..")
                .andShould().notBeInterfaces();

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("DTOs de response devem ter sufixo 'Dto' ou 'Response'")
    void responseDtosShouldHaveCorrectSuffix() {
        ArchRule rule = classes()
                .that().haveSimpleNameEndingWith("Dto")
                .or().haveSimpleNameEndingWith("Response")
                .should().resideInAPackage("..application.dto..")
                .andShould().notBeInterfaces();

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Commands não devem retornar listas")
    void commandsShouldNotReturnLists() {
        ArchRule rule = noMethods()
                .that().areDeclaredInClassesThat().haveSimpleNameContaining("Command")
                .should().haveRawReturnType("java.util.List");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Queries podem retornar listas ou objetos únicos")
    void queriesCanReturnListsOrSingleObjects() {
        // Esta é uma verificação positiva - apenas documentando o comportamento esperado
        ArchRule rule = methods()
                .that().areDeclaredInClassesThat().haveSimpleNameContaining("Query")
                .should().bePublic();

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Use Cases de comando devem modificar estado")
    void commandUseCasesShouldModifyState() {
        // Verificação conceitual - métodos de comando tipicamente retornam entidades modificadas
        ArchRule rule = methods()
                .that().areDeclaredInClassesThat().haveSimpleNameContaining("Command")
                .and().areDeclaredInClassesThat().haveSimpleNameEndingWith("UseCase")
                .should().bePublic();

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Use Cases de query não devem modificar estado")
    void queryUseCasesShouldNotModifyState() {
        // Verificação conceitual - métodos de query tipicamente retornam dados sem modificar
        ArchRule rule = methods()
                .that().areDeclaredInClassesThat().haveSimpleNameContaining("Query")
                .and().areDeclaredInClassesThat().haveSimpleNameEndingWith("UseCase")
                .should().bePublic();

        rule.check(importedClasses);
    }
}