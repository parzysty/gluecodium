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

package com.here.ivi.api.generator.common.cpp.templates;

import static org.junit.Assert.assertEquals;

import com.here.ivi.api.generator.common.TemplateEngine;
import com.here.ivi.api.model.cppmodel.CppComplexTypeRef;
import com.here.ivi.api.model.cppmodel.CppTypeDef;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public final class CppTypeDefTemplateTest {

  private static final String TEMPLATE_NAME = "cpp/CppTypeDef";

  private static final String TYPE_NAME = "Typical";
  private static final String TYPEDEF_NAME = "Definite";
  private static final String EXPECTED_TYPEDEF_RESULT =
      "using " + TYPEDEF_NAME + " = " + TYPE_NAME + ";\n";

  private final CppTypeDef cppTypeDef =
      new CppTypeDef(TYPEDEF_NAME, new CppComplexTypeRef.Builder(TYPE_NAME).build());

  @Test
  public void typeDefWithoutComment() {
    String result = TemplateEngine.render(TEMPLATE_NAME, cppTypeDef);

    assertEquals(EXPECTED_TYPEDEF_RESULT, result);
  }

  @Test
  public void typeDefWithComment() {
    cppTypeDef.comment = "nonsense";

    String result = TemplateEngine.render(TEMPLATE_NAME, cppTypeDef);

    final String expectedResult = "/**\n* nonsense\n*/\n" + EXPECTED_TYPEDEF_RESULT;
    assertEquals(expectedResult, result);
  }
}
