package schnatterinchen.labor.microstream;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;
import static org.assertj.core.api.Assertions.assertThat;

//@AnalyzeClasses(packages = "schnatterinchen.labor.microstream")
public class LayeredArchitectureTest {

    final Function<String, String> pkgName = (s) -> String.format("schnatterinchen.labor.microstream.%s", s);

    @Test
    void classes_inPackageApi_noDependenciesToOtherPackages() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages(pkgName.apply("api"));
        assertThat(importedClasses.size()).isEqualTo(1);

        ArchRule archRule = noClasses().that().resideInAPackage(pkgName.apply("api"))
                .should().dependOnClassesThat().resideInAPackage(pkgName.apply("persistence"));

        archRule.check(importedClasses);
    }

    @Test
    void classes_inPackageModel_noDependenciesToOtherPackages() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages(pkgName.apply("model"));
        assertThat(importedClasses.size()).isEqualTo(3);

        ArchRule archRule = noClasses().that().resideInAPackage(pkgName.apply("model"))
                .should().dependOnClassesThat().resideInAPackage(pkgName.apply("api"))
                .orShould().dependOnClassesThat().resideInAPackage(pkgName.apply("persistence"))
                .orShould().dependOnClassesThat().resideInAPackage(pkgName.apply("usecases"));

        archRule.check(importedClasses);
    }

    @Test
    void classes_inPackagePersistence_noDependenciesToOtherPackages() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages(pkgName.apply("persistence"));
        assertThat(importedClasses.size()).isEqualTo(3);

        ArchRule archRule = noClasses().that().resideInAPackage("..persistence..")
                .should().dependOnClassesThat().resideInAPackage("..api..") /*  */
                ;

        archRule.check(importedClasses);
        noClasses().that().resideInAPackage("..api..")
                .should().dependOnClassesThat().resideInAPackage("..model..");
    }

    @Test
    void layeredArchitectureTest_noDependenciesToOtherPackages() {
        ArchRule layerDependencies = layeredArchitecture()
                .layer("Api").definedBy(pkgName.apply("api"))
                .layer("Model").definedBy(pkgName.apply("model"))
                .layer("Persistence").definedBy(pkgName.apply("persistence"))
                .layer("Usecases").definedBy(pkgName.apply("useceases"))

                .whereLayer("Api").mayOnlyBeAccessedByLayers("Model","Persistence","Usecases")
                .whereLayer("Persistence").mayOnlyBeAccessedByLayers()
                .whereLayer("Model").mayNotBeAccessedByAnyLayer() /* should fail, does not */
                ;
    }
}
