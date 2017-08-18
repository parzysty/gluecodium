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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.doCallRealMethod;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import com.here.ivi.api.model.common.LazyInternalInclude;
import com.here.ivi.api.model.cppmodel.CppComplexTypeRef;
import com.here.ivi.api.model.cppmodel.CppPrimitiveTypeRef;
import com.here.ivi.api.model.cppmodel.CppTypeRef;
import com.here.ivi.api.model.franca.DefinedBy;
import com.here.ivi.api.model.franca.FrancaElement;
import java.util.Arrays;
import org.franca.core.franca.FBasicTypeId;
import org.franca.core.franca.FStructType;
import org.franca.core.franca.FTypeCollection;
import org.franca.core.franca.FTypeRef;
import org.franca.core.franca.FTypedElement;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DefinedBy.class, CppTypeMapper.class, CppNameRules.class})
public class CppTypeMapperArrayTest {

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    mockStatic(CppNameRules.class, DefinedBy.class);
  }

  @Test
  public void mapArrayOfPrimitiveType() {

    //mock franca elements
    FTypedElement typedElement = mock(FTypedElement.class);
    when(typedElement.isArray()).thenReturn(true);

    FTypeRef actualTypeRef = mock(FTypeRef.class);
    when(actualTypeRef.getPredefined()).thenReturn(FBasicTypeId.UINT32);

    when(typedElement.getType()).thenReturn(actualTypeRef);

    FrancaElement<?> mockFrancaModel = mock(FrancaElement.class);

    //act
    CppTypeRef result = CppTypeMapper.map(mockFrancaModel, typedElement);

    //assert
    assertTrue(result instanceof CppComplexTypeRef);
    CppComplexTypeRef complexResult = (CppComplexTypeRef) result;
    assertEquals(
        "::std::vector< " + CppPrimitiveTypeRef.Type.UINT32.getValue() + " >", complexResult.name);
    assertTrue(
        complexResult.includes.containsAll(
            Arrays.asList(CppLibraryIncludes.VECTOR, CppLibraryIncludes.INT_TYPES)));
  }

  @Test
  public void mapArrayOfComplexType() throws Exception {

    //mock franca elements
    FrancaElement<?> mockFrancaModel = mock(FrancaElement.class, Answers.RETURNS_DEEP_STUBS);
    FTypeCollection fTypeCollection = mock(FTypeCollection.class);
    when(mockFrancaModel.getFrancaTypeCollection()).thenReturn(fTypeCollection);

    FStructType structType = mock(FStructType.class);
    FTypeRef typeRef = mock(FTypeRef.class);
    when(typeRef.getDerived()).thenReturn(structType);
    when(structType.getName()).thenReturn("MyStruct");
    when(structType.eContainer()).thenReturn(fTypeCollection);

    FTypedElement typedElement = mock(FTypedElement.class);
    when(typedElement.isArray()).thenReturn(true);
    when(typedElement.getType()).thenReturn(typeRef);

    DefinedBy definer = mockDefinedBy(mockFrancaModel);

    LazyInternalInclude lazyInclude = new LazyInternalInclude(definer);
    whenNew(LazyInternalInclude.class).withArguments(definer).thenReturn(lazyInclude);

    //mock CppNameRules
    when(CppNameRules.getStructName(structType.getName())).thenReturn("MyStruct");
    when(CppNameRules.getNestedNameSpecifier(structType)).thenReturn(Arrays.asList("a", "b", "c"));

    //act
    CppTypeRef result = CppTypeMapper.map(mockFrancaModel, typedElement);

    //assert
    assertTrue(result instanceof CppComplexTypeRef);
    CppComplexTypeRef complexResult = (CppComplexTypeRef) result;
    assertEquals("::std::vector< ::a::b::c::MyStruct >", complexResult.name);
    assertTrue(
        complexResult.includes.containsAll(Arrays.asList(CppLibraryIncludes.VECTOR, lazyInclude)));
  }

  private static DefinedBy mockDefinedBy(FrancaElement<?> francaElement) {
    //DefinedBy's constructor is private, so static creator method is excluded from mocking
    //and utilized to create an instance of DefinedBy
    doCallRealMethod().when(DefinedBy.class);
    DefinedBy.createFromFrancaElement(any(FrancaElement.class));
    DefinedBy definer = DefinedBy.createFromFrancaElement(francaElement);
    when(DefinedBy.createFromFModelElement(any())).thenReturn(definer);
    return definer;
  }
}
