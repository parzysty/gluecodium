/*
 * Copyright (C) 2016-2020 HERE Europe B.V.
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

package com.here.gluecodium.generator.cbridge

import com.here.gluecodium.generator.common.ImportsResolver
import com.here.gluecodium.generator.common.Include
import com.here.gluecodium.generator.common.ReferenceMapBasedResolver
import com.here.gluecodium.model.lime.LimeAttributeType.EQUATABLE
import com.here.gluecodium.model.lime.LimeBasicType
import com.here.gluecodium.model.lime.LimeConstant
import com.here.gluecodium.model.lime.LimeContainer
import com.here.gluecodium.model.lime.LimeElement
import com.here.gluecodium.model.lime.LimeEnumeration
import com.here.gluecodium.model.lime.LimeException
import com.here.gluecodium.model.lime.LimeGenericType
import com.here.gluecodium.model.lime.LimeLambdaParameter
import com.here.gluecodium.model.lime.LimeList
import com.here.gluecodium.model.lime.LimeMap
import com.here.gluecodium.model.lime.LimeReturnType
import com.here.gluecodium.model.lime.LimeSet
import com.here.gluecodium.model.lime.LimeTypeRef
import com.here.gluecodium.model.lime.LimeTypedElement

internal class CBridgeHeaderIncludeResolver(
    limeReferenceMap: Map<String, LimeElement>
) : ReferenceMapBasedResolver(limeReferenceMap), ImportsResolver<Include> {

    override fun resolveElementImports(limeElement: LimeElement) =
        when (limeElement) {
            is LimeConstant -> emptyList()
            is LimeTypeRef -> resolveTypeRefIncludes(limeElement)
            is LimeReturnType -> resolveTypeRefIncludes(limeElement.typeRef)
            is LimeLambdaParameter -> resolveTypeRefIncludes(limeElement.typeRef)
            is LimeTypedElement -> resolveTypeRefIncludes(limeElement.typeRef)
            is LimeContainer -> resolveContainerIncludes(limeElement)
            is LimeEnumeration -> listOf(INT_INCLUDE)
            is LimeList -> resolveTypeRefIncludes(limeElement.elementType)
            is LimeSet -> resolveTypeRefIncludes(limeElement.elementType)
            is LimeMap -> resolveTypeRefIncludes(limeElement.keyType) + resolveTypeRefIncludes(limeElement.valueType)
            else -> emptyList()
        }

    private fun resolveContainerIncludes(limeContainer: LimeContainer) =
        when {
            limeContainer.attributes.have(EQUATABLE) -> listOf(BOOL_INCLUDE)
            else -> emptyList()
        }

    private fun resolveTypeRefIncludes(limeTypeRef: LimeTypeRef): List<Include> {
        if (limeTypeRef.isNullable) return emptyList()
        return when (val limeType = limeTypeRef.type.actualType) {
            is LimeBasicType -> resolveBasicTypeInclude(limeType.typeId)
            is LimeEnumeration -> listOf(INT_INCLUDE)
            is LimeGenericType -> resolveElementImports(limeType)
            is LimeException -> resolveTypeRefIncludes(limeType.errorType) + BOOL_INCLUDE
            else -> emptyList()
        }
    }

    private fun resolveBasicTypeInclude(typeId: LimeBasicType.TypeId) =
        when {
            typeId == LimeBasicType.TypeId.BOOLEAN -> listOf(BOOL_INCLUDE)
            typeId.isIntegerType -> listOf(INT_INCLUDE)
            else -> emptyList()
        }

    companion object {
        val BOOL_INCLUDE = Include.createSystemInclude("stdbool.h")
        val INT_INCLUDE = Include.createSystemInclude("stdint.h")
    }
}
