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

package com.here.ivi.api.generator.cbridge;

import static com.here.ivi.api.generator.common.NameHelper.toUpperCamelCase;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.apache.commons.lang3.ArrayUtils.addAll;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import com.here.ivi.api.model.franca.Interface;
import com.here.ivi.api.model.franca.TypeCollection;
import java.util.List;
import navigation.BaseApiSpec.InterfacePropertyAccessor;
import navigation.BaseApiSpec.TypeCollectionPropertyAccessor;
import org.franca.core.franca.FInterface;
import org.franca.core.franca.FMethod;
import org.franca.core.franca.FStructType;
import org.franca.core.franca.FTypeCollection;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CBridgeNameRulesTest {

  private static final List<String> PACKAGES = asList("PKG1", "PKG2");
  private static final String INTERFACE_NAME = "TestInterface";
  private static final String TYPE_COLLECTION_NAME = "TestTypeCollection";
  private static final String STRUCT_NAME = "structName";
  public static final String METHOD_NAME = "testMethod";

  @Mock(answer = Answers.RETURNS_DEEP_STUBS)
  private Interface<InterfacePropertyAccessor> anInterface;

  @Mock(answer = Answers.RETURNS_DEEP_STUBS)
  private TypeCollection<TypeCollectionPropertyAccessor> typeCollection;

  @Mock private FTypeCollection francaTypeCollection;
  @Mock private FInterface francaInterface;
  @Mock private FStructType francaStruct;
  @Mock private FMethod francaMethod;
  private CBridgeNameRules nameRules;

  @Before
  public void setUp() {
    when(anInterface.getModelInfo().getPackageNames()).thenReturn(PACKAGES);
    when(anInterface.getName()).thenReturn(INTERFACE_NAME);
    when(typeCollection.getModelInfo().getPackageNames()).thenReturn(PACKAGES);
    when(typeCollection.getName()).thenReturn(TYPE_COLLECTION_NAME);
    when(francaStruct.getName()).thenReturn(STRUCT_NAME);
    nameRules = new CBridgeNameRules();
  }

  @Test
  public void getStructNameCreatesProperNameForStructsInTypeCollections() {
    String expectedName =
        String.join(
            "_",
            (String[])
                addAll(
                    PACKAGES.toArray(new String[0]),
                    TYPE_COLLECTION_NAME,
                    toUpperCamelCase(STRUCT_NAME)));

    String actualName = nameRules.getStructName(typeCollection, francaStruct);

    assertEquals(expectedName, actualName);
  }

  @Test
  public void getStructNameCreatesProperNameForStructsInInterfaces() {
    String expectedName =
        String.join(
            "_",
            (String[])
                addAll(
                    PACKAGES.toArray(new String[0]),
                    INTERFACE_NAME,
                    toUpperCamelCase(STRUCT_NAME)));

    String actualName = nameRules.getStructName(anInterface, francaStruct);

    assertEquals(expectedName, actualName);
  }

  @Test
  public void getHandleNameCreatesProperName() {
    String expectedName =
        prepandNameWithPackageAndInterface(toUpperCamelCase(STRUCT_NAME) + "Ref", "_");

    String actualName = nameRules.getHandleName(anInterface, francaStruct);

    assertEquals(expectedName, actualName);
  }

  @Test
  public void getBaseApiNameCreatesProperName() {
    String expectedName =
        String.join(
            "::",
            (String[])
                addAll(
                    PACKAGES.toArray(new String[0]),
                    INTERFACE_NAME + "Stub",
                    toUpperCamelCase(STRUCT_NAME)));

    String actualName = nameRules.getBaseApiStructName(anInterface, francaStruct);

    assertEquals(expectedName, actualName);
  }

  @Test
  public void getMethodNameCreatesProperName() {
    when(francaMethod.getName()).thenReturn(METHOD_NAME);
    String expectedName = prepandNameWithPackageAndInterface(METHOD_NAME, "_");

    String actualName = nameRules.getMethodName(anInterface, francaMethod);
    assertEquals(expectedName, actualName);
  }

  @Test
  public void getMethodNameDoesNotThrowExceptionWhenPackageListIsSingletonList() {
    when(anInterface.getModelInfo().getPackageNames()).thenReturn(singletonList("PKG"));
    when(francaMethod.getName()).thenReturn(METHOD_NAME);

    nameRules.getMethodName(anInterface, francaMethod);
  }

  @Test
  public void getHeaderFileNameReturnsCorrectPath() {
    String expected = INTERFACE_NAME + ".h";

    String actual = nameRules.getHeaderFileName(anInterface);

    assertEquals(expected, actual);
  }

  @Test
  public void ImplementationFileNameReturnsCorrectPath() {
    String expected = INTERFACE_NAME + ".cpp";

    String actual = nameRules.getImplementationFileName(anInterface);

    assertEquals(expected, actual);
  }

  @Test
  public void getHeaderFileNameWithPathReturnsCorrectPath() {
    String expected = "cbridge/PKG1/PKG2/TestInterface.h";

    String actual = nameRules.getHeaderFileNameWithPath(anInterface);

    assertEquals(expected, actual);
  }

  @Test
  public void getImplementationFileNameWithPathReturnsCorrectPath() {
    String expected = "cbridge/PKG1/PKG2/TestInterface.cpp";

    String actual = nameRules.getImplementationFileNameWithPath(anInterface);

    assertEquals(expected, actual);
  }

  private String prepandNameWithPackageAndInterface(String name, String delimiter) {
    return String.join(
        delimiter, (String[]) addAll(PACKAGES.toArray(new String[0]), INTERFACE_NAME, name));
  }
}
