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

package com.here.ivi.api.model.rules;

import com.here.ivi.api.TranspilerExecutionException;
import com.here.ivi.api.model.FrancaModel;
import com.here.ivi.api.model.Interface;
import com.here.ivi.api.model.TypeCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;
import navigation.CppStubSpec;
import org.franca.core.franca.FConstantDef;
import org.franca.core.franca.FInterface;
import org.franca.core.franca.FMethod;
import org.franca.core.franca.FStructType;
import org.franca.core.franca.FType;

/**
 * This class contains methods that identify struct-with-method pairs and allows filtering the
 * FrancaModel accordingly.
 */
public class StructMethodRules {

  private static final String STRUCT_INITIALIZER_METHOD_NAME = "init";
  private static final String BELONGING_STRUCT_CONSTANT_NAME = "DefiningType";

  /**
   * Finds the FStruct that defines the whole TypeCollection
   *
   * @param tc The TypeCollection that will be searched
   * @return the struct or null if
   */
  public static FStructType findStructType(TypeCollection<?> tc) {

    FStructType memberStruct = null;
    for (FType type : tc.getFrancaTypeCollection().getTypes()) {
      if (type.getName().equals(BELONGING_STRUCT_CONSTANT_NAME) && type instanceof FStructType) {
        memberStruct = (FStructType) type;
        break;
      }
    }
    return memberStruct;
  }

  /**
   * Checks whether a method is a struct initializer
   *
   * @param method The method to check
   * @return true if the method is a struct initializer
   */
  public static boolean isStructInitializer(FMethod method) {
    return STRUCT_INITIALIZER_METHOD_NAME.equals(method.getName());
  }

  /**
   * Tests whether the Constant is actually pointing to a defining Struct
   *
   * @param constantDef The constant to check
   * @return true if the constant points to a struct
   */
  public static boolean isBelongingStruct(FConstantDef constantDef) {
    return BELONGING_STRUCT_CONSTANT_NAME.equals(constantDef.getType().getDerived().getName());
  }

  /**
   * A pair of TypeCollection & Interface to model structs with methods
   *
   * @param <IA> The Interface accessor
   * @param <TA> The TypeCollection accessor
   */
  public static class StructMethodPair<
      IA extends CppStubSpec.InterfacePropertyAccessor,
      TA extends CppStubSpec.TypeCollectionPropertyAccessor> {
    public final Interface<IA> iface;
    public final TypeCollection<TA> type;

    StructMethodPair(Interface<IA> iface, TypeCollection<TA> type) {
      this.iface = iface;
      this.type = type;
    }
  }

  /**
   * Divides the given model into three streams:
   *
   * <ul>
   *   <li>All bare interfaces
   *   <li>All type collections
   *   <li>All structs that have methods, a pair of iface & typecollection
   * </ul>
   *
   * <p>The elements are exclusive to each stream, so the
   *
   * @param model the input model
   * @param interfaceCollector the mapping function for all bare interfaces
   * @param typeCollector the mapping function for all type collections
   * @param structWithMethodCollector the mapping function the combination of iface & typecollection
   * @param <Result> The result type, share between all Functions
   * @param <IA> The Interface accessor
   * @param <TA> The TypeCollection accessor
   * @return a stream of Result
   */
  public static <
          Result,
          IA extends CppStubSpec.InterfacePropertyAccessor,
          TA extends CppStubSpec.TypeCollectionPropertyAccessor>
      Stream<Result> partitionModel(
          FrancaModel<IA, TA> model,
          Function<Interface<IA>, List<Result>> interfaceCollector,
          Function<TypeCollection<TA>, Result> typeCollector,
          Function<StructMethodPair<IA, TA>, Result> structWithMethodCollector) {

    List<StructMethodPair<IA, TA>> structMethodPairs = collectMethodContainers(model);

    return Stream.concat(
        Stream.concat(
            // generate one file for each type collection, containing all the typedefs, enums, etc.
            model
                .getTypeCollections()
                .stream()
                .filter(
                    tc ->
                        !tc.getPropertyAccessor()
                            .getIsStructDefinition(tc.getFrancaTypeCollection()))
                .map(typeCollector),

            // every interface (that is not a struct) gets its own file
            model
                .getInterfaces()
                .stream()
                .filter(
                    iface ->
                        iface.getPropertyAccessor().getIsMethodContainer(iface.getFrancaInterface())
                                == null
                            || !iface
                                .getPropertyAccessor()
                                .getIsMethodContainer(iface.getFrancaInterface()))
                .map(interfaceCollector)
                .flatMap(Collection::stream)),
        structMethodPairs.stream().map(structWithMethodCollector));
  }

  /**
   * Goes through all typeCollections of the model, takes the ones that are a struct definition and
   * collects the related interfaces into a central list.
   *
   * @param model the input model
   * @param <IA> The Interface accessor
   * @param <TA> The TypeCollection accessor
   * @return A list of interface typeCollection pairs
   */
  private static <
          IA extends CppStubSpec.InterfacePropertyAccessor,
          TA extends CppStubSpec.TypeCollectionPropertyAccessor>
      List<StructMethodPair<IA, TA>> collectMethodContainers(final FrancaModel<IA, TA> model) {

    List<StructMethodPair<IA, TA>> result = new ArrayList<>();

    // for each type collection: search its methods (if any)
    for (TypeCollection<TA> tc : model.getTypeCollections()) {

      if (tc.getPropertyAccessor().getIsStructDefinition(tc.getFrancaTypeCollection())) {
        // find real interface
        FInterface fi =
            tc.getPropertyAccessor().getBelongingMethodContainer(tc.getFrancaTypeCollection());

        if (fi != null) {
          Optional<Interface<IA>> fiOptional =
              model
                  .getInterfaces()
                  .stream()
                  .filter(i -> fi.equals(i.getFrancaInterface()))
                  .findFirst();

          if (fiOptional.isPresent()) {
            result.add(new StructMethodPair<>(fiOptional.orElse(null), tc));
          } else {
            throw new TranspilerExecutionException(
                String.format("FrancaModel.Interface could not be found: %s.", fi));
          }
        } else {
          result.add(new StructMethodPair<>(null, tc));
        }
      }
    }

    return result;
  }
}
