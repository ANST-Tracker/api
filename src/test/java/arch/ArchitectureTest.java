package arch;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.junit.Before;
import org.junit.Test;

public class ArchitectureTest {
    private static final String FW_LAYER = "..sd.api.fw..";
    private static final String APP_LAYER = "..sd.api.app..";
    private static final String API_LAYER = "..sd.api.app.api..";
    private static final String IMPL_LAYER = "..sd.api.app.impl..";
    private static final String DOMAIN_LAYER = "..domain..";
    private JavaClasses importedClasses;

    @Before
    public void setup() {
        importedClasses = new ClassFileImporter()
                .importPackages("com.anst.sd.api");
    }

    @Test
    public void fwShouldOnlyAccessApp() {
        ArchRule rule = ArchRuleDefinition
                .classes()
                .that()
                .resideInAnyPackage(FW_LAYER)
                .should()
                .onlyAccessClassesThat()
                .resideInAnyPackage("..security..", "..aspect..", "..spring..", "org.springframework..", "org.aspectj..", "lombok..", "jakarta..", "org.redisson..", "com.fasterxml..", "io.swagger..", "java..", "org.slf4j..", APP_LAYER);
        rule.check(importedClasses);
    }

    @Test
    public void apiShouldOnlyAccessDomain() {
        ArchRule rule = ArchRuleDefinition
                .classes()
                .that()
                .resideInAPackage(API_LAYER)
                .should()
                .onlyAccessClassesThat()
                .resideInAnyPackage(API_LAYER, IMPL_LAYER, DOMAIN_LAYER, "java..", "org.springframework..", "lombok..", "com.fasterxml.jackson..");
        rule.check(importedClasses);
    }

    @Test
    public void domainShouldNotAccessOtherPackages() {
        ArchRule rule = ArchRuleDefinition
                .classes()
                .that()
                .resideInAnyPackage(DOMAIN_LAYER)
                .should()
                .onlyAccessClassesThat()
                .resideInAnyPackage(
                        DOMAIN_LAYER, "java..", "org.hibernate..");
        rule.check(importedClasses);
    }
}
