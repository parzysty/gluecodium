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
import com.here.ivi.api.generator.common.templates.CppStructWithMethodsTemplate;
import com.here.ivi.api.generator.cppstub.StubCommentParser;
import com.here.ivi.api.generator.cppstub.templates.StructCtor;
import com.here.ivi.api.model.*;
import com.here.ivi.api.model.cppmodel.*;
import com.here.ivi.api.model.rules.DefaultValuesRules;
import com.here.ivi.api.model.rules.StructMethodRules;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import navigation.CppStubSpec;
import org.franca.core.franca.*;

public class StructWithMethodsGenerator {

  private final CppNameRules nameRules;

  private static final Logger logger = Logger.getLogger(StructWithMethodsGenerator.class.getName());

  public StructWithMethodsGenerator(CppNameRules rules) {
    this.nameRules = rules;
  }

  public GeneratedFile generate(
      final GeneratorSuite suite,
      final FrancaModel<?, ?> model,
      final Interface<?> methods,
      final TypeCollection<?> typeCollection) {

    CppNamespace ns = generateCppModel(methods, typeCollection);
    String outputFile = nameRules.getHeaderPath(typeCollection);
    CppIncludeResolver resolver = new CppIncludeResolver(model, outputFile);
    resolver.resolveLazyIncludes(ns);

    CharSequence generatorNotice =
        CppGeneratorHelper.generateGeneratorNotice(suite, typeCollection, outputFile);
    CharSequence innerContent =
        CppDelegatorTemplate.generate(
            new CppTemplateDelegator() {
              public CharSequence generate(CppClass cppClass) {
                return CppStructWithMethodsTemplate.generate(this, cppClass);
              }
            },
            ns);
    String fileContent =
        CppCommentHeaderTemplate.generate(generatorNotice, innerContent).toString();

    return new GeneratedFile(fileContent, outputFile);
  }

  private CppNamespace generateCppModel(Interface<?> methods, TypeCollection<?> typeCollection) {

    List<CppNamespace> packageNs =
        CppGeneratorHelper.packageToCppNamespace(
            nameRules.convertPackageToNamespace(typeCollection.getPackage()));

    CppClass newClass = generateClass(methods, typeCollection);

    // add to innermost namespace
    Iterables.getLast(packageNs).members.add(newClass);

    // return outermost namespace
    return Iterables.getFirst(packageNs, null);
  }

  private CppClass generateClass(final Interface<?> api, final TypeCollection<?> typeCollection) {

    CppClass newClass = new CppClass(nameRules.getStructName(typeCollection.getName()));

    // nested enums //////////////////////////
    for (FType type : typeCollection.getFrancaTypeCollection().getTypes()) {
      if (type instanceof FEnumerationType) {
        newClass.enums.add(
            TypeGenerationHelper.buildCppEnumClass(nameRules, (FEnumerationType) type));
      }
    }

    // find member struct ///////////////////////////
    FStructType memberStruct = StructMethodRules.findStructType(typeCollection);

    if (memberStruct == null) {
      logger.warning("Failed to find type struct! ");
      return newClass;
    }

    newClass.comment = StubCommentParser.parse(memberStruct).getMainBodyText();

    // default values of members //////////////////////////

    FCompoundInitializer defaultInitializer = null;
    for (FConstantDef constantDef : typeCollection.getFrancaTypeCollection().getConstants()) {
      // only structs of the same type as belonging interface with correct name will be checked
      if (DefaultValuesRules.isStructDefaultValueConstant(constantDef)
          && StructMethodRules.isBelongingStruct(constantDef)) {
        // is valid as constantDef was parsed as a struct ...
        defaultInitializer = (FCompoundInitializer) constantDef.getRhs();
        break;
      }
    }

    CppModelAccessor<?> rootType = new CppModelAccessor<>(typeCollection, nameRules);

    // if no specific defaults are defined, generate fields without any addition
    if (defaultInitializer == null) {
      logger.info("Failed to find default values of " + memberStruct.getName());
      for (FField fieldInfo : memberStruct.getElements()) {
        CppField field = TypeGenerationHelper.buildCppField(rootType, fieldInfo, null);
        field.comment = StubCommentParser.parse(fieldInfo).getMainBodyText();
        newClass.fields.add(field);
      }
    } else {
      // generate fields /////////////////////////////////
      Iterator<FField> memberIterator = memberStruct.getElements().iterator();
      Iterator<FFieldInitializer> valueIterator = defaultInitializer.getElements().iterator();
      while (memberIterator.hasNext() && valueIterator.hasNext()) {
        FField fieldInfo = memberIterator.next();
        FFieldInitializer value = valueIterator.next();
        CppField field = TypeGenerationHelper.buildCppField(rootType, fieldInfo, value);
        field.comment = StubCommentParser.parse(fieldInfo).getMainBodyText();
        newClass.fields.add(field);
      }
    }

    // methods ////////////////////////////
    //////////////////////////////////////

    // default constructor is added via xtend template ...

    generateNonDefaultConstructors(newClass, api);

    // constants
    for (FConstantDef constantDef : typeCollection.getFrancaTypeCollection().getConstants()) {

      // skip all default values in the generation
      if (DefaultValuesRules.isStructDefaultValueConstant(constantDef)) {
        continue;
      }

      CppConstant constant = TypeGenerationHelper.buildCppConstant(rootType, constantDef);
      if (constant.isValid()) {
        constant.comment = StubCommentParser.parse(constantDef).getMainBodyText();
        newClass.constants.add(constant);
      } else {
        throw new TranspilerExecutionException(
            String.format(
                "Failed generating constant! %s %s.",
                constantDef.getName(), constantDef.getRhs().getClass()));
      }
    }

    return newClass;
  }

  private void generateNonDefaultConstructors(CppClass newClass, final Interface<?> api) {
    if (api == null) {
      return;
    }

    CppModelAccessor<? extends CppStubSpec.InterfacePropertyAccessor> rootModelIf =
        new CppModelAccessor<>(api, nameRules);

    // non default-constructors ...
    StructCtor templateCtor = new StructCtor();
    api.getFrancaInterface()
        .getMethods()
        .stream()
        .filter(StructMethodRules::isStructInitializer)
        .forEach(
            method -> {
              CppMethod nonDefaultCtor = new CppMethod();
              nonDefaultCtor.bodyTemplate = templateCtor;
              nonDefaultCtor.name = newClass.name;
              nonDefaultCtor.returnType = CppType.None;

              final List<FArgument> inArgs = method.getInArgs();
              if (inArgs.size() == 1) {
                nonDefaultCtor.specifiers.add(CppMethod.Specifier.EXPLICIT);
              }

              nonDefaultCtor.comment = StubCommentParser.parse(method).getMainBodyText();

              for (FArgument argument : method.getInArgs()) {
                CppParameter param = new CppParameter();
                param.name = nameRules.getArgumentName(argument.getName());
                param.type = CppTypeMapper.map(rootModelIf, argument);
                param.mode = CppParameter.Mode.Input;
                nonDefaultCtor.inParameters.add(param);
              }

              newClass.methods.add(nonDefaultCtor);
            });
  }
}
