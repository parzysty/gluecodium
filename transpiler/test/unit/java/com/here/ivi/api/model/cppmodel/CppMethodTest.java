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

package com.here.ivi.api.model.cppmodel;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import com.here.ivi.api.generator.common.cpp.templates.MethodBodyTemplate;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.junit.Test;

public class CppMethodTest {
  private CppMethod cppMethod = new CppMethod();

  @Test
  public void generateBodyWithNullMethodBodyTemplate() {
    cppMethod.bodyTemplate = null;

    assertNull(cppMethod.generateBody());
  }

  @Test
  public void generateBodyWithMockMethodBodyTemplate() {
    MethodBodyTemplate methodBodyTemplate = mock(MethodBodyTemplate.class);
    when(methodBodyTemplate.generate(cppMethod)).thenReturn(new StringConcatenation());

    cppMethod.bodyTemplate = methodBodyTemplate;

    assertNotNull(cppMethod.generateBody());
    verify(methodBodyTemplate).generate(cppMethod);
  }
}
