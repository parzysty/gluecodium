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

import static com.here.ivi.api.generator.cbridge.CppTypeInfo.TypeCategory.ARRAY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.here.ivi.api.model.common.InstanceRules;
import org.franca.core.franca.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(InstanceRules.class)
public class CBridgeArrayMapperTest {

  @Mock private FTypeRef francaTypeRef;
  @Mock private FStructType francaStructType;
  @Mock private FArrayType francaArray;

  @Mock(answer = Answers.RETURNS_DEEP_STUBS)
  private FTypeDef francaTypeDef;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    PowerMockito.mockStatic(InstanceRules.class);
  }

  @Test
  public void structArrayName() {
    when(francaStructType.getName()).thenReturn("StructTest");

    String arrayName = CArrayMapper.getName(francaStructType);

    assertEquals("Should have the same name", "StructTest", arrayName);
  }

  @Test
  public void typeDefArrayName() {
    when(francaTypeDef.getActualType().getDerived()).thenReturn(null);
    when(francaTypeDef.getActualType().getPredefined()).thenReturn(FBasicTypeId.STRING);

    String arrayName = CArrayMapper.getName(francaTypeDef);

    assertEquals("Should have the same name", "String", arrayName);
  }

  @Test
  public void typeDefInstanceIdArrayName() {
    when(InstanceRules.isInstanceId(any(FTypeDef.class))).thenReturn(true);
    when(francaTypeDef.getName()).thenReturn("TypeDefTest");

    String arrayName = CArrayMapper.getName(francaTypeDef);

    assertEquals("Should have the same name", "TypeDefTest", arrayName);

    PowerMockito.verifyStatic();
    InstanceRules.isInstanceId(francaTypeDef);
  }

  @Test
  public void typeRefArrayNameWithDerived() {
    when(francaStructType.getName()).thenReturn("StructTest");
    when(francaTypeRef.getDerived()).thenReturn(francaStructType);

    String arrayName = CArrayMapper.getName(francaTypeRef);

    assertEquals("Should have the same name", "StructTest", arrayName);
  }

  @Test
  public void typeRefArrayNameWithoutDerived() {
    when(francaTypeRef.getPredefined()).thenReturn(FBasicTypeId.INT32);

    String arrayName = CArrayMapper.getName(francaTypeRef);

    assertEquals("Should have the same name", "Int32", arrayName);
  }

  @Test
  public void arrayName() {
    when(francaTypeRef.getPredefined()).thenReturn(FBasicTypeId.INT32);
    when(francaArray.getElementType()).thenReturn(francaTypeRef);

    String arrayName = CArrayMapper.getName(francaArray);

    assertEquals("Should have the same name", "Int32", arrayName);
  }

  @Test
  public void getNameForEnums() {
    assertEquals("Enums", CArrayMapper.getName(mock(FEnumerationType.class)));
  }

  @Test
  public void unknownTypeReturnNull() {
    assertNull("Should return null", CArrayMapper.getName(null));
  }

  @Test
  public void createArrayCppTypeInfo() {
    when(francaStructType.getName()).thenReturn("StructTest");
    when(francaTypeRef.getDerived()).thenReturn(francaStructType);

    CppTypeInfo innerType = CppTypeInfo.STRING;
    CppTypeInfo arrayType = CArrayMapper.createArrayReference(innerType);

    assertNotNull("Array type should not be null", arrayType);
    assertNotNull("Inner type should not be null", arrayType.innerType);
    assertEquals("Should return array type", ARRAY, arrayType.typeCategory);
    assertEquals("Return type name should match", "_baseRef", arrayType.functionReturnType.name);
  }

  @Test
  public void createNestedArrayCppTypeInfo() {
    when(francaStructType.getName()).thenReturn("StructTest");
    when(francaTypeRef.getDerived()).thenReturn(francaStructType);

    CppTypeInfo innerType = CppTypeInfo.STRING;
    CppTypeInfo arrayType = CArrayMapper.createArrayReference(innerType);
    CppTypeInfo nestedArrayType = CArrayMapper.createArrayReference(arrayType);

    assertNotNull("Array type should not be null", nestedArrayType);
    assertNotNull("Inner type should not be null", nestedArrayType.innerType);
    assertEquals("Should return array type", ARRAY, nestedArrayType.typeCategory);
    assertEquals("Should return array type", ARRAY, nestedArrayType.innerType.typeCategory);
    assertEquals(
        "Return type name should match", "_baseRef", nestedArrayType.functionReturnType.name);
  }
}
