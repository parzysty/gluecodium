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

package com.here.gluecodium.generator.java

import com.here.gluecodium.cli.GluecodiumExecutionException
import com.here.gluecodium.common.LimeLogger
import com.here.gluecodium.generator.common.CommentsProcessor
import com.here.gluecodium.generator.common.NameResolver
import com.here.gluecodium.generator.common.ReferenceMapBasedResolver
import com.here.gluecodium.model.lime.LimeAttributeType.JAVA
import com.here.gluecodium.model.lime.LimeAttributeValueType.FUNCTION_NAME
import com.here.gluecodium.model.lime.LimeBasicType
import com.here.gluecodium.model.lime.LimeBasicType.TypeId
import com.here.gluecodium.model.lime.LimeComment
import com.here.gluecodium.model.lime.LimeDirectTypeRef
import com.here.gluecodium.model.lime.LimeElement
import com.here.gluecodium.model.lime.LimeExternalDescriptor.Companion.NAME_NAME
import com.here.gluecodium.model.lime.LimeFunction
import com.here.gluecodium.model.lime.LimeGenericType
import com.here.gluecodium.model.lime.LimeLambda
import com.here.gluecodium.model.lime.LimeList
import com.here.gluecodium.model.lime.LimeMap
import com.here.gluecodium.model.lime.LimeNamedElement
import com.here.gluecodium.model.lime.LimeParameter
import com.here.gluecodium.model.lime.LimeProperty
import com.here.gluecodium.model.lime.LimeReturnType
import com.here.gluecodium.model.lime.LimeSet
import com.here.gluecodium.model.lime.LimeType
import com.here.gluecodium.model.lime.LimeTypeAlias
import com.here.gluecodium.model.lime.LimeTypeRef
import com.here.gluecodium.model.lime.LimeTypedElement
import com.here.gluecodium.model.lime.LimeValue

internal class JavaNameResolver(
    limeReferenceMap: Map<String, LimeElement>,
    private val basePackages: List<String>,
    private val javaNameRules: JavaNameRules,
    private val limeLogger: LimeLogger,
    private val commentsProcessor: CommentsProcessor,
    private val signatureResolver: JavaSignatureResolver
) : ReferenceMapBasedResolver(limeReferenceMap), NameResolver {

    private val valueResolver = JavaValueResolver(this)
    private val duplicateNames: Set<String>
    private val limeToJavaNames: Map<String, String>

    val typesWithDuplicateNames: Set<String>

    init {
        val duplicateNamesMap = buildDuplicateNames()
        duplicateNames = duplicateNamesMap.keys
        typesWithDuplicateNames = duplicateNamesMap.values.flatten().map { it.fullName }.toSet()
        limeToJavaNames = buildPathMap()
    }

    override fun resolveName(element: Any): String =
        when (element) {
            is LimeComment -> resolveComment(element)
            is LimeValue -> valueResolver.resolveValue(element)
            is LimeType -> resolveTypeName(element)
            is LimeTypeRef -> resolveTypeRef(element)
            is LimeReturnType -> resolveTypeRef(element.typeRef)
            is LimeFunction -> resolveFunctionName(element)
            is LimeNamedElement -> javaNameRules.getName(element)
            else -> throw GluecodiumExecutionException("Unsupported element type ${element.javaClass.name}")
        }

    override fun resolveGetterName(element: Any) = resolveAccessorName(element) { getGetterName(it) }

    override fun resolveSetterName(element: Any) = resolveAccessorName(element) { getSetterName(it) }

    override fun resolveReferenceName(element: Any) =
        when (element) {
            is LimeTypeRef -> resolveTypeRef(element)
            is LimeType -> resolveTypeRef(LimeDirectTypeRef(element))
            else -> null
        }

    private fun resolveComment(limeComment: LimeComment): String {
        val commentText = limeComment.getFor("Java")
        if (commentText.isBlank()) return ""

        val exactElement = limeReferenceMap[limeComment.path.toString()] as? LimeNamedElement

        val commentedElement = exactElement ?: getParentElement(limeComment.path, withSuffix = true)
        return commentsProcessor.process(commentedElement.fullName, commentText, limeToJavaNames, limeLogger)
    }

    private fun resolveAccessorName(element: Any, rule: JavaNameRules.(LimeTypedElement) -> String) =
        (element as? LimeTypedElement)?.let { javaNameRules.rule(it) }

    internal fun resolveTypeRef(limeTypeRef: LimeTypeRef, needsBoxing: Boolean = false): String {
        val limeType = limeTypeRef.type.actualType
        val externalName = limeType.external?.java?.get(NAME_NAME)
        return when {
            externalName != null -> externalName
            limeType is LimeGenericType -> resolveGenericTypeRef(limeType)
            limeType !is LimeBasicType -> {
                val nestedName = resolveNestedNames(limeType).joinToString(".")
                if (duplicateNames.contains(nestedName)) (resolvePackageNames(limeType) + nestedName).joinToString(".")
                else nestedName
            }
            needsBoxing || limeTypeRef.isNullable -> resolveBoxedBasicType(limeType.typeId)
            else -> resolveBasicType(limeType.typeId)
        }
    }

    private fun resolveGenericTypeRef(limeType: LimeGenericType) =
        when (limeType) {
            is LimeList -> {
                val elementTypeName = resolveTypeRef(limeType.elementType, needsBoxing = true)
                "List<$elementTypeName>"
            }
            is LimeSet -> {
                val elementTypeName = resolveTypeRef(limeType.elementType, needsBoxing = true)
                "Set<$elementTypeName>"
            }
            is LimeMap -> {
                val keyTypeName = resolveTypeRef(limeType.keyType, needsBoxing = true)
                val valueTypeName = resolveTypeRef(limeType.valueType, needsBoxing = true)
                "Map<$keyTypeName, $valueTypeName>"
            }
            else -> throw GluecodiumExecutionException("Unsupported element type ${limeType.javaClass.name}")
        }

    private fun resolveTypeName(limeType: LimeType) =
        when (val actualType = limeType.actualType) {
            is LimeBasicType -> resolveBasicType(actualType.typeId)
            else -> javaNameRules.getName(actualType)
        }

    private fun resolveFunctionName(limeFunction: LimeFunction): String {
        val parentElement = getParentElement(limeFunction)
        return when {
            parentElement is LimeLambda -> parentElement.attributes.get(JAVA, FUNCTION_NAME) ?: "apply"
            parentElement is LimeProperty && limeFunction === parentElement.setter -> resolveSetterName(parentElement)
            parentElement is LimeProperty -> resolveGetterName(parentElement)
            else -> javaNameRules.getName(limeFunction)
        } ?: throw GluecodiumExecutionException("Unsupported parent element ${parentElement.path}")
    }

    private fun resolveNestedNames(limeElement: LimeNamedElement): List<String> {
        val elementName = javaNameRules.getName(limeElement)
        val parentElement = if (limeElement.path.hasParent) getParentElement(limeElement) else null
        return when {
            parentElement != null -> resolveNestedNames(parentElement) + elementName
            else -> listOf(elementName)
        }
    }

    private fun resolveBoxedBasicType(typeId: TypeId) =
        when (typeId) {
            TypeId.INT8 -> "Byte"
            TypeId.UINT8, TypeId.INT16 -> "Short"
            TypeId.UINT16, TypeId.INT32 -> "Integer"
            TypeId.UINT32, TypeId.INT64, TypeId.UINT64 -> "Long"
            TypeId.BOOLEAN -> "Boolean"
            TypeId.FLOAT -> "Float"
            TypeId.DOUBLE -> "Double"
            else -> resolveBasicType(typeId)
        }

    private fun resolveBasicType(typeId: TypeId) =
        when (typeId) {
            TypeId.VOID -> "void"
            TypeId.INT8 -> "byte"
            TypeId.UINT8, TypeId.INT16 -> "short"
            TypeId.UINT16, TypeId.INT32 -> "int"
            TypeId.UINT32, TypeId.INT64, TypeId.UINT64 -> "long"
            TypeId.BOOLEAN -> "boolean"
            TypeId.FLOAT -> "float"
            TypeId.DOUBLE -> "double"
            TypeId.STRING -> "String"
            TypeId.BLOB -> "byte[]"
            TypeId.DATE -> "Date"
            TypeId.DURATION -> "Duration"
            TypeId.LOCALE -> "Locale"
        }

    private fun buildPathMap(): Map<String, String> {
        val result = limeReferenceMap.values
            .filterIsInstance<LimeNamedElement>()
            .filterNot { it is LimeProperty || it is LimeFunction || it is LimeTypeAlias || it is LimeParameter }
            .associateBy({ it.path.toAmbiguousString() }, { resolveFullName(it) })
            .toMutableMap()

        result += limeReferenceMap.values.filterIsInstance<LimeParameter>()
            .associateBy({ it.fullName }, { resolveFullName(it) })

        limeReferenceMap.values.filterIsInstance<LimeFunction>().forEach {
            val ambiguousKey = it.path.toAmbiguousString()
            val fullSignatureKey = ambiguousKey +
                it.parameters.joinToString(prefix = "(", postfix = ")", separator = ",") {
                    parameter ->
                    parameter.typeRef.toString()
                }
            val fullName = resolveFullName(it) + getSignatureSuffix(it)

            result[it.fullName] = fullName
            result[ambiguousKey] = fullName
            result[fullSignatureKey] = fullName
        }

        limeReferenceMap.values.filterIsInstance<LimeProperty>().forEach {
            val pathKey = it.path.toAmbiguousString()
            val parentName = resolveFullName(getParentElement(it))
            val getterName = parentName + "#" + resolveGetterName(it)!!

            result[pathKey] = getterName
            result["$pathKey.get"] = getterName
            if (it.setter != null) {
                result["$pathKey.set"] = parentName + "#" + resolveSetterName(it)!!
            }
        }

        return result
    }

    fun resolveFullName(limeElement: LimeNamedElement, forceDelimiter: String? = null): String {
        val elementName = resolveName(limeElement)

        if (!limeElement.path.hasParent) {
            return (resolvePackageNames(limeElement) + elementName).joinToString(".")
        }

        val parentElement = getParentElement(limeElement)
        val prefix = resolveFullName(parentElement)

        val delimiter = when {
            forceDelimiter != null -> forceDelimiter
            limeElement is LimeType -> "."
            else -> "#"
        }
        val ownName =
            if (limeElement is LimeFunction && limeElement.isConstructor) resolveName(parentElement) else elementName
        return prefix + delimiter + ownName
    }

    fun resolvePackageNames(limeElement: LimeNamedElement) =
        (basePackages + limeElement.path.head).map { normalizePackageName(it) }

    private fun getSignatureSuffix(limeFunction: LimeFunction) =
        when {
            shouldGenerateSignatureWithArguments(limeFunction) ->
                limeFunction.parameters.joinToString(prefix = "(", postfix = ")") { resolveName(it.typeRef) }

            else -> ""
        }

    private fun shouldGenerateSignatureWithArguments(limeFunction: LimeFunction) =
        signatureResolver.isOverloaded(limeFunction) || limeFunction.isConstructor

    private fun buildDuplicateNames() =
        limeReferenceMap.values
            .filterIsInstance<LimeType>()
            .filterNot { it is LimeTypeAlias }
            .filter { it.external?.java == null }
            .groupBy { resolveNestedNames(it).joinToString(".") }
            .filterValues { it.size > 1 }

    companion object {
        fun normalizePackageName(name: String) = name.replace("_", "")
    }
}
