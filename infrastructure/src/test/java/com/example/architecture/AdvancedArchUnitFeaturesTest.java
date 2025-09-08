package com.example.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.freeze.FreezingArchRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;

/**
 * Demonstração dos recursos avançados do ArchUnit 1.4.1.
 * Mostra funcionalidades como FreezingArchRule, métricas de arquitetura e
 * outras funcionalidades da versão mais recente.
 */
class AdvancedArchUnitFeaturesTest {

    private JavaClasses importedClasses;

    @BeforeEach
    void setUp() {
        importedClasses = new ClassFileImporter()
                .importPackages("com.example");
    }

    @Test
    @DisplayName("Demonstra FreezingArchRule - congela violações existentes")
    void demonstrateFreezingArchRule() {
        // Esta é uma regra que pode ter violações existentes em projetos legados
        ArchRule strictRule = noClasses()
                .that().resideInAPackage("..domain..")
                .should().dependOnClassesThat()
                .resideInAnyPackage("..spring..", "..jakarta..");

        // Congela as violações existentes - apenas novas violações falharão
        ArchRule frozenRule = FreezingArchRule.freeze(strictRule);
        
        // Este teste passará mesmo se existirem violações,
        // mas falhará se novas violações forem introduzidas
        frozenRule.check(importedClasses);
    }

    @Test
    @DisplayName("Verifica que campos não sejam públicos (regra aprimorada)")
    void fieldsShouldNotBePublic() {
        // Usando recursos melhorados da versão 1.4.1
        ArchRule rule = fields()
                .that().areNotStatic()
                .and().areNotFinal()
                .and().areDeclaredInClassesThat().areNotEnums()
                .should().notBePublic()
                .allowEmptyShould(true);

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Verifica uso de Optional em retornos de métodos")
    void methodsShouldReturnOptionalForNullableValues() {
        ArchRule rule = methods()
                .that().arePublic()
                .and().haveNameMatching("find.*")
                .and().doNotHaveRawReturnType(void.class)
                .and().areDeclaredInClassesThat().resideInAPackage("..repository..")
                .should().haveRawReturnType("java.util.Optional")
                .orShould().haveRawReturnType("java.util.List")
                .allowEmptyShould(true);

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Verifica convenções avançadas de nomenclatura")
    void advancedNamingConventions() {
        // Regra mais sofisticada usando recursos da versão 1.4.1
        ArchRule rule = classes()
                .that().areAnnotatedWith("org.springframework.stereotype.Service")
                .and().areNotInterfaces()
                .should().haveSimpleNameEndingWith("Service")
                .andShould().resideInAPackage("..service..")
                .andShould().notBeAnnotatedWith("org.springframework.stereotype.Repository")
                .allowEmptyShould(true);

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Demonstra verificação de dependências transitivas")
    void transitiveDependencyCheck() {
        // Verifica dependências mais complexas
        ArchRule rule = noClasses()
                .that().resideInAPackage("..domain..")
                .should().accessClassesThat()
                .resideInAnyPackage("..web..", "..rest..", "..controller..")
                .orShould().accessClassesThat()
                .areAnnotatedWith("org.springframework.web.bind.annotation.RestController")
                .allowEmptyShould(true);

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Verifica que classes de configuração sejam adequadas")
    void configurationClassesShouldFollowConventions() {
        ArchRule rule = classes()
                .that().areAnnotatedWith("org.springframework.context.annotation.Configuration")
                .should().haveSimpleNameEndingWith("Configuration")
                .andShould().bePublic()
                .allowEmptyShould(true);

        rule.check(importedClasses);
    }
}