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

package com.here.gluecodium.generator.common

import com.here.gluecodium.model.lime.LimeContainer
import com.here.gluecodium.model.lime.LimeContainerWithInheritance
import com.here.gluecodium.model.lime.LimeException
import com.here.gluecodium.model.lime.LimeFunction
import com.here.gluecodium.model.lime.LimeLambda
import com.here.gluecodium.model.lime.LimeNamedElement
import com.here.gluecodium.model.lime.LimeStruct
import com.here.gluecodium.model.lime.LimeType
import com.here.gluecodium.model.lime.LimeTypeAlias
import com.here.gluecodium.model.lime.LimeTypeHelper
import com.here.gluecodium.model.lime.LimeTypeRef

internal class GenericImportsCollector<T>(
    private val importsResolver: ImportsResolver<T>,
    private val collectTypeRefImports: Boolean = false,
    private val collectOwnImports: Boolean = false,
    private val collectParentImports: Boolean = false,
    private val collectTypeAliasImports: Boolean = false
) : ImportsCollector<T> {

    override fun collectImports(limeElement: LimeNamedElement): List<T> {
        val allTypes = LimeTypeHelper.getAllTypes(limeElement)
        val typeRefImports =
            if (collectTypeRefImports)
                collectTypeRefs(allTypes).flatMap { importsResolver.resolveElementImports(it) }
            else emptyList()
        val ownImports =
            if (collectOwnImports) allTypes.flatMap { importsResolver.resolveElementImports(it) } else emptyList()
        val parentImports =
            if (collectParentImports)
                collectParentTypeRefs(limeElement, allTypes).flatMap { importsResolver.resolveElementImports(it) }
            else emptyList()
        val typeAliasImports =
            if (collectTypeAliasImports)
                allTypes.filterIsInstance<LimeTypeAlias>().flatMap { importsResolver.resolveElementImports(it.typeRef) }
            else emptyList()
        val constantImports = allTypes.filterIsInstance<LimeContainer>().flatMap { it.constants }
            .flatMap { importsResolver.resolveElementImports(it) }

        return typeRefImports + ownImports + parentImports + typeAliasImports + constantImports
    }

    private fun collectTypeRefs(allTypes: List<LimeType>): List<LimeTypeRef> {
        val containers = allTypes.filterIsInstance<LimeContainer>()
        val functions = containers.flatMap { it.functions }
        val properties = containers.flatMap { it.properties }
        val lambdas = allTypes.filterIsInstance<LimeLambda>()
        val exceptions = allTypes.filterIsInstance<LimeException>()
        val structs = allTypes.filterIsInstance<LimeStruct>()
        return structs.flatMap { it.fields }.map { it.typeRef } +
            functions.flatMap { collectTypeRefs(it) } + properties.map { it.typeRef } +
            lambdas.flatMap { collectTypeRefs(it.asFunction()) } +
            exceptions.map { it.errorType }
    }

    private fun collectTypeRefs(limeFunction: LimeFunction) =
        limeFunction.parameters.map { it.typeRef } +
            limeFunction.returnType.typeRef +
            listOfNotNull(limeFunction.thrownType?.typeRef, limeFunction.exception?.errorType)

    private fun collectParentTypeRefs(limeElement: LimeNamedElement, allTypes: List<LimeType>): List<LimeTypeRef> {
        val parentTypeRef = (limeElement as? LimeContainerWithInheritance)?.parent ?: return emptyList()
        val containers = allTypes.filterIsInstance<LimeContainerWithInheritance>()
        return containers.flatMap { it.inheritedFunctions }.flatMap { collectTypeRefs(it) } +
            containers.flatMap { it.inheritedProperties }.map { it.typeRef } + parentTypeRef
    }
}
