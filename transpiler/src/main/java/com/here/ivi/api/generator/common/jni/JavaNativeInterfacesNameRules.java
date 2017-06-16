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

package com.here.ivi.api.generator.common.jni;

import com.here.ivi.api.generator.android.AndroidGeneratorSuite;
import com.here.ivi.api.generator.common.java.JavaNameRules;
import com.here.ivi.api.model.javamodel.JavaClass;
import com.here.ivi.api.model.javamodel.JavaPackage;
import java.io.File;

public final class JavaNativeInterfacesNameRules {
  private static final String JNI_HEADER_FILE_ENDING = ".h";
  private static final String JNI_IMPLEMENTATION_FILE_ENDING = ".cpp";

  // TODO: Fetch package root from JavaClass!
  private static final String JAVA_PACKAGE_ROOT = "com.here.ivi";
  private static final String UNDERSCORE = "_";

  public static String getHeaderFileName(final JavaClass javaClass) {
    return AndroidGeneratorSuite.GENERATOR_NAMESPACE
        + File.separator
        + "jni"
        + File.separator
        + JAVA_PACKAGE_ROOT.replace(JavaNameRules.JAVA_PACKAGE_SEPARATOR, UNDERSCORE)
        + UNDERSCORE
        + javaClass.name
        + JNI_HEADER_FILE_ENDING;
  }

  public static String getImplementationFileName(final JavaClass javaClass) {
    return AndroidGeneratorSuite.GENERATOR_NAMESPACE
        + File.separator
        + "jni"
        + File.separator
        + JAVA_PACKAGE_ROOT.replace(JavaNameRules.JAVA_PACKAGE_SEPARATOR, UNDERSCORE)
        + UNDERSCORE
        + javaClass.name
        + JNI_IMPLEMENTATION_FILE_ENDING;
  }

  public static String getParameterName(final String javaParameterName) {
    if (javaParameterName == null || javaParameterName.isEmpty()) {
      return "";
    }

    return "j" + javaParameterName;
  }

  public static String getNativeParameterName(final String javaParameterName) {
    if (javaParameterName == null || javaParameterName.isEmpty()) {
      return "";
    }

    return "n" + javaParameterName;
  }

  public static String getPackageName(final JavaPackage javaPackage) {
    if (javaPackage == null || javaPackage.name == null || javaPackage.name.isEmpty()) {
      return "";
    }

    return javaPackage.name.replace('.', UNDERSCORE.charAt(0));
  }
}
