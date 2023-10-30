package arch;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

class ArchitectureTest {
    private JavaClasses importedClasses;

    @Before
    public void setup() {
        importedClasses = new ClassFileImporter()
                .importPackages("com.anst.sd.api");
    }

    @Test
    @DisplayName("Пакет fw имеет доступ только к пакету app")
    public void fwShouldOnlyAccessApp() {
        ArchRule rule = ArchRuleDefinition
                .classes()
                .that()
                .resideInAnyPackage("..sd.api.fw..")
                .should()
                .onlyAccessClassesThat()
                .resideInAnyPackage("..security..", "..aspect..", "..spring..", "org.springframework..", "org.aspectj..", "lombok..", "jakarta..", "org.redisson..", "com.fasterxml..", "io.swagger..", "java..", "org.slf4j..", "..sd.api.app..");
        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Пакет api имеет доступ только к пакету domain")
    public void apiShouldOnlyAccessDomain() {
        ArchRule rule = ArchRuleDefinition
                .classes()
                .that()
                .resideInAPackage("..sd.api.app.api")
                .should()
                .onlyAccessClassesThat()
                .resideInAnyPackage("..api.app.api..", "..api.app.impl..", "..domain..", "java..", "org.springframework..", "lombok..", "com.fasterxml.jackson..");
        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Пакет domain не имеет доступ к внешним пакетам")
    public void domainShouldNotAccessOtherPackages() {
        ArchRule rule = ArchRuleDefinition
                .classes()
                .that()
                .resideInAnyPackage("..domain..")
                .should()
                .onlyAccessClassesThat()
                .resideInAnyPackage(
                        "..domain..", "java..", "org.hibernate..");
        rule.check(importedClasses);
    }
}
