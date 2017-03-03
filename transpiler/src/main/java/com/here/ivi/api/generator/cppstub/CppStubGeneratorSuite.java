package com.here.ivi.api.generator.cppstub;

import com.here.ivi.api.Transpiler;
import com.here.ivi.api.generator.legacy.LegacyGenerator;
import com.here.ivi.api.generator.common.GeneratedFile;
import com.here.ivi.api.generator.common.GeneratorSuite;
import com.here.ivi.api.generator.common.Version;
import com.here.ivi.api.generator.cppstub.templates.CppStubNameRules;
import com.here.ivi.api.loader.FrancaModelLoader;
import com.here.ivi.api.loader.SpecAccessorFactory;
import com.here.ivi.api.loader.cppstub.CppStubSpecAccessorFactory;
import com.here.ivi.api.model.FrancaModel;
import com.here.ivi.api.model.ModelHelper;
import com.here.ivi.api.validator.common.BasicValidator;
import com.here.ivi.api.validator.cppstub.CppStubValidator;
import navigation.CppStubSpec;
import org.apache.commons.io.IOUtils;
import org.eclipse.emf.ecore.resource.ResourceSet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * This generator will build all the CppStubs that will have to be implemented on the client side @ref StubGenerator
 * as well as the data used by this stubs @ref TypeCollectionGenerator.
 *
 * It is the underlying generator, that all others depend on, as they will invoke the actual implementation through
 * the Stub interfaces.
 *
 * @startuml
 *
 *   title Generators
 *
 *   rectangle "Legacy Generator" {
 *
 *   rectangle PC #FFE [
 *   Public APIs
 *
 *   (.h)
 *   ]
 *   rectangle PrC [
 *   Public Impl
 *
 *   (.cpp)
 *   ]
 *   rectangle PIMPL [
 *   Private Impl
 *
 *   (.h, .cpp)
 *   ]
 *
 *   PC -down-> PrC : calls
 *   PrC -down-> PIMPL : calls
 *   }
 *
 *   rectangle "iOS Generator" {
 *
 *   rectangle Swift #FFE [
 *   Public APIs
 *
 *   (.swift)
 *   ]
 *   rectangle ObjectiveC [
 *   ObjectiveC
 *
 *   (.h, .mm)
 *   ]
 *
 *   Swift -down-> ObjectiveC : calls
 *   }
 *
 *   rectangle "Android Generator" {
 *
 *   rectangle Java #FFE [
 *   Public APIs
 *
 *   (.java)
 *   ]
 *   rectangle JNI [
 *   JNI
 *
 *   (.h, .cpp)
 *   ]
 *
 *   Java -down-> JNI : calls
 *   }
 *
 *   rectangle "CppStub Generator" {
 *   rectangle CS [
 *   Cpp Stub
 *
 *   (.h)
 *   ]
 *
 *   rectangle CT #FFE [
 *   Cpp Types
 *
 *   (.h)
 *   ]
 *   }
 *
 *   rectangle "Manual Implementation" {
 *   rectangle CTI #4CE [
 *   Cpp Types Implementation
 *
 *   (.incl)
 *   ]
 *
 *   rectangle CSI #4CE [
 *   Cpp Stub Implementation
 *
 *   (.cpp)
 *   ]
 *   }
 *   rectangle "Implementation Stack" {
 *   rectangle Legacy #2AC [
 *   Legacy
 *   ]
 *   }
 *
 *   CT .down.> CTI : uses
 *   CS .right.> CT  : uses
 *   CSI -up-|> CS : implements
 *
 *   CSI -down-> Legacy : calls
 *
 *   JNI -> CS : calls
 *   JNI ..> CT : uses
 *
 *   PIMPL --> CS : calls
 *   PC --|> CT : contains
 *
 *   ObjectiveC -> CS : calls
 *
 * @enduml
 */
public class CppStubGeneratorSuite
        implements GeneratorSuite<CppStubSpec.InterfacePropertyAccessor,CppStubSpec.TypeCollectionPropertyAccessor> {
    private final Transpiler tool;
    private final CppStubValidator validator = new CppStubValidator();
    private FrancaModel<CppStubSpec.InterfacePropertyAccessor, CppStubSpec.TypeCollectionPropertyAccessor> model;
    private FrancaModelLoader<CppStubSpec.InterfacePropertyAccessor, CppStubSpec.TypeCollectionPropertyAccessor> fml;
    private Collection<File> currentFiles;

    static Logger logger = java.util.logging.Logger.getLogger(CppStubGeneratorSuite.class.getName());

    public CppStubGeneratorSuite(Transpiler tp) {
        this.tool = tp;
    }

    @Override
    public List<GeneratedFile> generate() {
        //TODO add model null check

        CppStubNameRules rules = new CppStubNameRules();

        Stream<GeneratedFile> generatorStreams = Stream.concat(
                // generate one file for each type collection, containing all the typedefs, enums, etc.
                model.typeCollections.stream()
                        .map(tc -> {
                            TypeCollectionGenerator generator = new TypeCollectionGenerator(this, model, rules, tc);
                            return generator.generate();
                        }),

                // every interface gets its own file
                model.interfaces.stream()
                        .map(iface -> {
                            StubGenerator generator = new StubGenerator(this, model, rules, iface);
                            return generator.generate();
                        })
        );


        List<GeneratedFile> list = generatorStreams.filter(Objects::nonNull).collect(Collectors.toList());
        list.add(copyTarget("here/internal/expected.h", "src/"));

        return list;
    }

    private static GeneratedFile copyTarget(String fileName, String target) {
        InputStream stream = CppStubGeneratorSuite.class.getClassLoader().getResourceAsStream(fileName);

        if (stream != null) {
            try {
                String content = IOUtils.toString(stream, Charset.defaultCharset());
                return new GeneratedFile(content, target + File.separator + fileName);
            } catch (IOException ignored) {
            }
        }

        logger.severe("Failed loading resource " + fileName);

        return null;
    }

    @Override
    public Transpiler getTool() {
        return tool;
    }

    @Override
    public Version getVersion() {
        return new Version(0, 0, 1);
    }

    @Override
    public String getName() {
        return "com.here.CppStubGenerator";
    }

    @Override
    public SpecAccessorFactory<CppStubSpec.InterfacePropertyAccessor, CppStubSpec.TypeCollectionPropertyAccessor> createModelAccessorFactory() {
        return new CppStubSpecAccessorFactory();
    }

    @Override
    public void buildModel(String inputPath){
        final SpecAccessorFactory<CppStubSpec.InterfacePropertyAccessor, CppStubSpec.TypeCollectionPropertyAccessor>
                specAccessorFactory = createModelAccessorFactory();

        // load model
        fml = new FrancaModelLoader<>(specAccessorFactory);

        ModelHelper.getFdeplInjector().injectMembers(fml);
        currentFiles = FrancaModelLoader.listFilesRecursively(new File(inputPath));

        model = fml.load(specAccessorFactory.getSpecPath(), inputPath);
    }

    @Override
    public boolean validate() {
        ResourceSet rs = fml.getResourceSetProvider().get();
        return BasicValidator.validate(rs, currentFiles) && validator.validate(model);
    }
}
