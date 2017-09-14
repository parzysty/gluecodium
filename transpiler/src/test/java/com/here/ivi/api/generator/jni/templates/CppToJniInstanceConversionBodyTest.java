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

package com.here.ivi.api.generator.jni.templates;

import static org.junit.Assert.assertEquals;

import com.here.ivi.api.generator.common.TemplateEngine;
import com.here.ivi.api.model.jni.JniContainer;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class CppToJniInstanceConversionBodyTest {
  private static final String CPP_NAME = "CppName";
  private static final String JAVA_NAME = "JavaName";
  private static final List<String> JAVA_PACKAGES = Arrays.asList("java", "b", "c");
  private static final List<String> CPP_PACKAGES = Arrays.asList("cpp", "b", "c");

  private JniContainer jniContainer;

  @Before
  public void setUp() {
    jniContainer =
        JniContainer.createInterfaceContainer(JAVA_PACKAGES, CPP_PACKAGES, JAVA_NAME, CPP_NAME);
  }

  @Test
  public void generateMethodBody() {
    String expected =
        "{\n"
            + "    auto javaClass = env->FindClass(\""
            + String.join("/", JAVA_PACKAGES)
            + "/"
            + JAVA_NAME
            + "\");\n"
            + "    auto pInstanceSharedPointer = new std::shared_ptr<::"
            + String.join("::", CPP_PACKAGES)
            + "::"
            + CPP_NAME
            + ">(ninput);\n"
            + "    return create_instance_object(env, javaClass, reinterpret_cast<jlong> (pInstanceSharedPointer));\n"
            + "}";

    String generated = TemplateEngine.render("jni/CppToJniInstanceConversionBody", jniContainer);

    assertEquals(expected, generated);
  }
}
