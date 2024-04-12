package arch;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaModifier;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.library.Architectures;
import org.apache.logging.log4j.util.Strings;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@AnalyzeClasses(
        packages = "com.anst.sd.api",
        importOptions = {ImportOption.DoNotIncludeTests.class}
)
public class ArchitectureTest {
    public static final String FW_LAYER = "FW";
    public static final String ADAPTER_LAYER = "ADAPTER";
    public static final String APP_API_LAYER = "API";
    public static final String APP_IMPL_LAYER = "IMPL";
    public static final String DOMAIN_LAYER = "DOMAIN";

    @ArchTest
    public void adapterLayerTest(JavaClasses classes) {
        getLayers()
                .whereLayer(ADAPTER_LAYER)
                .mayOnlyBeAccessedByLayers(FW_LAYER)
                .check(classes);
    }

    @ArchTest
    public void appApiLayerTest(JavaClasses classes) {
        getLayers()
                .whereLayer(APP_API_LAYER)
                .mayOnlyAccessLayers(DOMAIN_LAYER)
                .check(classes);
    }

    @ArchTest
    public void appImplLayerTest(JavaClasses classes) {
        getLayers()
                .whereLayer(APP_IMPL_LAYER)
                .mayOnlyBeAccessedByLayers(FW_LAYER)
                .check(classes);
    }

    @ArchTest
    public void domainLayerTest(JavaClasses classes) {
        getLayers()
                .whereLayer(DOMAIN_LAYER)
                .mayNotAccessAnyLayer()
                .check(classes);
    }

    @ArchTest
    public void fwTest(JavaClasses classes) {
        getLayers()
                .whereLayer(FW_LAYER)
                .mayNotBeAccessedByAnyLayer()
                .check(classes);
    }

    @ArchTest
    public void useCaseTransactionalTest(JavaClasses classes) {
        List<String> errors = new ArrayList<>();
        classes.stream()
                .filter(clazz -> clazz.getPackageName().contains("app.impl") &&
                        clazz.getSimpleName().toLowerCase().endsWith("usecase"))
                .flatMap(clazz -> clazz.getMethods().stream()
                        .filter(method -> method.getModifiers().contains(JavaModifier.PUBLIC)))
                .forEach(method -> {
                    try {
                        method.getAnnotationOfType(Transactional.class);
                    } catch (Exception e) {
                        errors.add(e.getMessage());
                    }
                });
        if (!errors.isEmpty()) {
            throw new AssertionError(Strings.join(errors, '\n'));
        }
    }

    // ===================================================================================================================
    // = Implementation
    // ===================================================================================================================

    private Architectures.LayeredArchitecture getLayers() {
        return Architectures.layeredArchitecture()
                .consideringOnlyDependenciesInLayers()
                .layer(ADAPTER_LAYER).definedBy("..api.adapter..")
                .layer(APP_API_LAYER).definedBy("..api.app.api..")
                .layer(APP_IMPL_LAYER).definedBy("..api.app.impl..")
                .layer(DOMAIN_LAYER).definedBy("..api.domain..")
                .layer(FW_LAYER).definedBy("..api.fw..");
    }
}
