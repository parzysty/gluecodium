/*
 * Copyright (C) 2016-2021 HERE Europe B.V.
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

package com.here.gluecodium.generator.cpp

import com.here.gluecodium.generator.common.PlatformSignatureResolver
import com.here.gluecodium.model.lime.LimeAttributeType.CPP
import com.here.gluecodium.model.lime.LimeAttributeValueType.TYPE
import com.here.gluecodium.model.lime.LimeContainerWithInheritance
import com.here.gluecodium.model.lime.LimeElement
import com.here.gluecodium.model.lime.LimeFunction
import com.here.gluecodium.model.lime.LimeParameter
import com.here.gluecodium.model.lime.LimeTypeRef

internal class CppSignatureResolver(
    limeReferenceMap: Map<String, LimeElement>,
    nameRules: CppNameRules
) : PlatformSignatureResolver(limeReferenceMap, CPP, nameRules, emptySet()) {

    override fun getNullableSuffix(limeTypeRef: LimeTypeRef) =
        when {
            !limeTypeRef.isNullable -> ""
            limeTypeRef.type.actualType is LimeContainerWithInheritance -> ""
            else -> "?"
        }

    override fun getParameterSignature(limeParameter: LimeParameter) =
        limeParameter.typeRef.attributes.get(CPP, TYPE) ?: super.getParameterSignature(limeParameter)

    fun getInheritedOverloads(limeFunction: LimeFunction): List<LimeFunction> {
        val container = getContainer(limeFunction) as? LimeContainerWithInheritance ?: return emptyList()
        val functionName = getFunctionName(limeFunction)
        return container.inheritedFunctions.filter { !it.isStatic }.filter { getFunctionName(it) == functionName }
    }
}
