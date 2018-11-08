/*
 * Copyright (C) 2016-2018 HERE Europe B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 * License-Filename: LICENSE
 */

package com.here.genium.generator.node;

import com.here.genium.generator.common.NameHelper;
import com.here.genium.model.franca.DefinedBy;
import java.io.File;
import org.franca.core.franca.FArgument;
import org.franca.core.franca.FMethod;
import org.franca.core.franca.FTypeCollection;

public final class NodeNameRules {

  public static final String TARGET_DIRECTORY = "node" + File.separator;

  private NodeNameRules() {}

  public static String getImplementationFileName(final FTypeCollection francaTypeCollection) {
    return TARGET_DIRECTORY
        + String.join(File.separator, DefinedBy.getPackages(francaTypeCollection))
        + File.separator
        + getFileName(francaTypeCollection)
        + ".cpp";
  }

  private static String getFileName(final FTypeCollection francaTypeCollection) {
    return NodeNameRules.getClassName(francaTypeCollection.getName());
  }

  public static String getMethodName(final FMethod method) {
    return NameHelper.toLowerCamelCase(method.getName());
  }

  public static String getParameterName(final FArgument argument) {
    return NameHelper.toLowerCamelCase(argument.getName());
  }

  public static String getClassName(final String name) {
    return NameHelper.toUpperCamelCase(name);
  }
}