/*
 * Copyright (C) 2017 HERE Global B.V. and its affiliate(s). All rights reserved.
 *
 * This software, including documentation, is protected by copyright controlled by
 * HERE Global B.V. All rights are reserved. Copying, including reproducing, storing,
 * adapting or translating, any or all of this material requires the prior written
 * consent of HERE Global B.V. This material also contains confidential information,
 * which may not be disclosed to others without prior written consent of HERE Global B.V.
 *
 */

package com.here.ivi.api.generator.common.cpp;

import com.google.common.collect.Iterables;
import com.here.ivi.api.TranspilerExecutionException;
import com.here.ivi.api.generator.common.GeneratedFile;
import com.here.ivi.api.generator.common.GeneratorSuite;
import com.here.ivi.api.generator.common.templates.CppCommentHeaderTemplate;
import com.here.ivi.api.generator.common.templates.CppDelegatorTemplate;
import com.here.ivi.api.generator.cppstub.StubCommentParser;
import com.here.ivi.api.model.DefinedBy;
import com.here.ivi.api.model.FrancaModel;
import com.here.ivi.api.model.TypeCollection;
import com.here.ivi.api.model.cppmodel.*;
import com.here.ivi.api.model.rules.InstanceRules;
import java.util.List;
import navigation.CppStubSpec;
import org.franca.core.franca.*;

/**
 * This generator will create the cpp data types for any type mentioned in a typeCollection. The cpp
 * data structs, enums and typedefs are then used by the CppStubGenerator.
 *
 * <p>Individual generators on top of the CppStubGenerator can then decide to either wrap these
 * types (e.g. Java + JNI, Swift, ...) or use them directly (traditional legacy).
 */
public class TypeCollectionGenerator {

  private final GeneratorSuite suite;
  private final FrancaModel<?, ?> coreModel;
  private final CppNameRules nameRules;
  private final TypeCollection<?> tc;
  private final CppModelAccessor<?> rootModel;

  public <TA extends CppStubSpec.TypeCollectionPropertyAccessor> TypeCollectionGenerator(
      GeneratorSuite suite,
      FrancaModel<? extends CppStubSpec.InterfacePropertyAccessor, TA> coreModel,
      CppNameRules rules,
      TypeCollection<TA> tc) {
    this.nameRules = rules;
    this.suite = suite;
    this.coreModel = coreModel;
    this.tc = tc;

    // this is the main type of the file, all namespaces and includes have to be resolved relative to it
    rootModel = new CppModelAccessor<>(tc, nameRules);
  }

  public GeneratedFile generate() {
    CppNamespace model = buildCppModel();

    if (model.isEmpty()) {
      return null;
    }

    String outputFile = nameRules.getHeaderPath(tc);

    // find included files and resolve relative to generated path
    CppIncludeResolver resolver = new CppIncludeResolver(coreModel, outputFile);
    resolver.resolveLazyIncludes(model);

    Object generatorNotice = CppGeneratorHelper.generateGeneratorNotice(suite, tc, outputFile);
    Object innerContent = CppDelegatorTemplate.generate(new CppTemplateDelegator(), model);
    String fileContent =
        CppCommentHeaderTemplate.generate(generatorNotice, innerContent).toString();

    return new GeneratedFile(fileContent, outputFile);
  }

  private CppNamespace buildCppModel() {
    List<CppNamespace> packageNs =
        CppGeneratorHelper.packageToCppNamespace(
            nameRules.convertPackageToNamespace(tc.getPackage()));

    CppNamespace result =
        new CppNamespace(nameRules.getTypeCollectionName(tc.getFrancaTypeCollection()));

    for (FType type : tc.getFrancaTypeCollection().getTypes()) {
      // struct
      if (type instanceof FStructType) {
        result.members.add(buildCppStruct((FStructType) type));
      } else if (type instanceof FTypeDef) {

        FTypeDef typeDef = (FTypeDef) type;
        // skip the typedefs for instanceId typedefs as these types are purely internal
        if (InstanceRules.isInstanceId(typeDef)) {
          continue;
        }
        result.members.add(buildTypeDef(typeDef));
      } else if (type instanceof FArrayType) {
        result.members.add(buildArray((FArrayType) type));
      } else if (type instanceof FMapType) {
        result.members.add(buildMap((FMapType) type));
      } else if (type instanceof FEnumerationType) {
        result.members.add(
            TypeGenerationHelper.buildCppEnumClass(nameRules, (FEnumerationType) type));
      } else {
        throw new TranspilerExecutionException(
            String.format("Missing type map in %s for %s.", rootModel, type.getClass().getName()));
      }
    }

    // constants
    for (FConstantDef constantDef : tc.getFrancaTypeCollection().getConstants()) {
      CppConstant constant = TypeGenerationHelper.buildCppConstant(rootModel, constantDef);
      constant.comment = StubCommentParser.parse(constantDef).getMainBodyText();

      if (constant.isValid()) {
        result.members.add(constant);
      } else {
        throw new TranspilerExecutionException(
            String.format(
                "Failed generating constant! %s %s.",
                constantDef.getName(), constantDef.getRhs().getClass()));
      }
    }

    // ensure to not create empty namespaces
    if (!result.isEmpty()) {
      // add to innermost namespace
      Iterables.getLast(packageNs).members.add(result);
    }

    // return outermost namespace
    return Iterables.getFirst(packageNs, null);
  }

  private CppElement buildMap(FMapType type) {
    CppTypeDef typeDef = new CppTypeDef();
    typeDef.comment = StubCommentParser.parse(type).getMainBodyText();
    typeDef.name = nameRules.getTypedefName(type.getName());
    typeDef.targetType =
        CppTypeMapper.wrapMapType(
            DefinedBy.createFromFModelElement(type),
            CppTypeMapper.map(rootModel, type.getKeyType()),
            CppTypeMapper.map(rootModel, type.getValueType()),
            nameRules);

    return typeDef;
  }

  private CppElement buildTypeDef(FTypeDef type) {
    CppTypeDef typeDef = new CppTypeDef();
    typeDef.comment = StubCommentParser.parse(type).getMainBodyText();
    typeDef.name = nameRules.getTypedefName(type.getName());
    typeDef.targetType = CppTypeMapper.map(rootModel, type.getActualType());

    return typeDef;
  }

  private CppElement buildArray(FArrayType type) {
    CppTypeDef typeDef = new CppTypeDef();
    typeDef.comment = StubCommentParser.parse(type).getMainBodyText();
    typeDef.name = nameRules.getTypedefName(type.getName());
    typeDef.targetType = CppTypeMapper.defineArray(rootModel, type);
    return typeDef;
  }

  private CppStruct buildCppStruct(FStructType structType) {
    CppStruct struct = new CppStruct();
    struct.comment = StubCommentParser.parse(structType).getMainBodyText();
    struct.name = nameRules.getStructName(structType.getName());

    for (FField fieldInfo : structType.getElements()) {
      CppField field = TypeGenerationHelper.buildCppField(rootModel, fieldInfo, null);
      field.comment = StubCommentParser.parse(fieldInfo).getMainBodyText();
      struct.fields.add(field);
    }

    return struct;
  }
}
