/*
 * Copyright (C) 2016-2019 HERE Europe B.V.
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

package com.here.gluecodium.generator.jni

import com.here.gluecodium.generator.common.modelbuilder.LimeTreeWalker
import com.here.gluecodium.generator.cpp.CppIncludeResolver
import com.here.gluecodium.generator.cpp.CppLibraryIncludes
import com.here.gluecodium.generator.cpp.CppModelBuilder
import com.here.gluecodium.generator.cpp.CppNameResolver
import com.here.gluecodium.generator.cpp.CppNameRules
import com.here.gluecodium.generator.cpp.CppTypeMapper
import com.here.gluecodium.generator.java.JavaModelBuilder
import com.here.gluecodium.generator.java.JavaNameResolver
import com.here.gluecodium.generator.java.JavaNameRules
import com.here.gluecodium.generator.java.JavaSignatureResolver
import com.here.gluecodium.generator.java.JavaTypeMapper
import com.here.gluecodium.generator.java.JavaValueMapper
import com.here.gluecodium.model.common.Include
import com.here.gluecodium.model.java.JavaCustomType
import com.here.gluecodium.model.java.JavaImport
import com.here.gluecodium.model.java.JavaPackage
import com.here.gluecodium.model.jni.JniContainer
import com.here.gluecodium.model.jni.JniElement
import com.here.gluecodium.model.jni.JniEnum
import com.here.gluecodium.model.jni.JniStruct
import com.here.gluecodium.model.jni.JniTopLevelElement
import com.here.gluecodium.model.lime.LimeElement
import com.here.gluecodium.model.lime.LimeNamedElement

class JniGenerator(
    private val limeReferenceMap: Map<String, LimeElement>,
    private val basePackages: List<String>,
    private val internalPackageList: List<String>,
    private val additionalIncludes: List<String>,
    private val enableAndroidFeatures: Boolean,
    private val internalNamespace: List<String>,
    rootNamespace: List<String>,
    private val nonNullAnnotation: JavaCustomType?,
    private val nullableAnnotation: JavaCustomType?,
    private val cppNameRules: CppNameRules,
    private val javaNameRules: JavaNameRules
) {
    private val cppNameResolver = CppNameResolver(rootNamespace, limeReferenceMap, cppNameRules)
    private val javaNameResolver = JavaNameResolver(javaNameRules, limeReferenceMap)

    fun generateModel(rootElement: LimeNamedElement): JavaModel {
        val basePackage = JavaPackage(basePackages)
        val internalPackage = JavaPackage(basePackages + internalPackageList)
        val javaTypeMapper = JavaTypeMapper(
            limeReferenceMap = limeReferenceMap,
            basePackage = basePackage,
            internalPackage = internalPackage,
            serializationBase = if (enableAndroidFeatures) PARCELABLE else null,
            nonNullAnnotation = nonNullAnnotation,
            nullableAnnotation = nullableAnnotation,
            nameResolver = javaNameResolver
        )
        val javaBuilder = JavaModelBuilder(
            rootPackage = basePackage.createChildPackage(rootElement.path.head),
            typeMapper = javaTypeMapper,
            valueMapper = JavaValueMapper(limeReferenceMap, javaNameRules, javaTypeMapper),
            nameRules = javaNameRules,
            nameResolver = javaNameResolver
        )

        val includeResolver = CppIncludeResolver(limeReferenceMap, cppNameRules)
        val typeMapper = CppTypeMapper(cppNameResolver, includeResolver, internalNamespace)
        val cppBuilder = CppModelBuilder(
            typeMapper = typeMapper,
            nameResolver = cppNameResolver,
            includeResolver = includeResolver,
            limeReferenceMap = limeReferenceMap
        )

        val jniBuilder = JniModelBuilder(
            javaBuilder = javaBuilder,
            javaSignatureResolver = JavaSignatureResolver(limeReferenceMap, javaNameRules),
            cppBuilder = cppBuilder,
            cppIncludeResolver = includeResolver,
            internalNamespace = internalNamespace,
            buildTransientModel = { generateModel(it).jniContainers }
        )

        val treeWalker = LimeTreeWalker(listOf(javaBuilder, cppBuilder, jniBuilder))
        treeWalker.walkTree(rootElement)

        val jniToLimeMap =
            jniBuilder.referenceMap.entries.associate { it.value to limeReferenceMap[it.key] }
        val jniResults = jniBuilder.finalResults.mapNotNull {
            wrapInContainer(it, jniToLimeMap, includeResolver)
        }
        jniResults.forEach { it.includes.addAll(getIncludes(it)) }

        return JavaModel(
            javaBuilder.referenceMap,
            javaBuilder.finalResults,
            jniResults,
            jniBuilder.setsCollector.values.toSet()
        )
    }

    private fun wrapInContainer(
        jniElement: JniElement,
        jniToLimeMap: Map<JniElement, LimeElement?>,
        includeResolver: CppIncludeResolver
    ) =
        when (jniElement) {
            is JniContainer -> jniElement
            is JniStruct -> {
                val jniContainer = wrapInContainer(
                    jniElement,
                    jniElement.cppFullyQualifiedName,
                    jniToLimeMap,
                    includeResolver
                )
                jniContainer.add(jniElement)
                jniContainer
            }
            is JniEnum -> {
                val jniContainer = wrapInContainer(
                    jniElement,
                    jniElement.cppEnumName,
                    jniToLimeMap,
                    includeResolver
                )
                jniContainer.add(jniElement)
                jniContainer
            }
            else -> null
        }

    private fun wrapInContainer(
        jniElement: JniTopLevelElement,
        cppFullyQualifiedName: String,
        jniToLimeMap: Map<JniElement, LimeElement?>,
        includeResolver: CppIncludeResolver
    ): JniContainer {
        val jniContainer = JniContainer(
            javaPackages = jniElement.javaPackage.packageNames,
            cppNameSpaces = cppFullyQualifiedName.split("::").dropLast(1),
            containerType = JniContainer.ContainerType.TYPE_COLLECTION,
            internalNamespace = internalNamespace
        )
        (jniToLimeMap[jniElement] as? LimeNamedElement)?.let {
            jniContainer.includes += includeResolver.resolveIncludes(it)
        }
        return jniContainer
    }

    private fun getIncludes(jniContainer: JniContainer): List<Include> {
        val includes = mutableListOf<String>()
        if (jniContainer.containerType != JniContainer.ContainerType.TYPE_COLLECTION) {
            includes +=
                JniNameRules.getHeaderFileName(JniNameRules.getJniClassFileName(jniContainer))
        }
        includes += jniContainer.structs
            .filterNot { it.methods.isEmpty() }
            .map {
                JniNameRules.getHeaderFileName(JniNameRules.getJniStructFileName(jniContainer, it))
            }
        includes += additionalIncludes

        val includeSet = includes.map { Include.createInternalInclude(it) }.toSet()
        CppLibraryIncludes.filterIncludes(includeSet, internalNamespace)
        return includeSet.sorted()
    }

    companion object {
        private val ANDROID_OS_PACKAGE = JavaPackage(listOf("android", "os"))
        private val PARCELABLE = JavaCustomType(
            fullName = "Parcelable",
            packageNames = ANDROID_OS_PACKAGE.packageNames,
            imports = LinkedHashSet(listOf(
                JavaImport("Parcelable", ANDROID_OS_PACKAGE),
                JavaImport("Parcel", ANDROID_OS_PACKAGE)
            ))
        )
    }
}