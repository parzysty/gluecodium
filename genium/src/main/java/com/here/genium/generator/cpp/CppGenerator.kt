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

package com.here.genium.generator.cpp

import com.here.genium.generator.common.GeneratedFile
import com.here.genium.generator.common.templates.TemplateEngine
import com.here.genium.model.common.Include
import com.here.genium.model.cpp.CppConstant
import com.here.genium.model.cpp.CppExternableElement
import com.here.genium.model.cpp.CppFile
import com.here.genium.model.cpp.CppUsing
import java.nio.file.Paths

class CppGenerator(private val pathPrefix: String, private val internalNamespace: List<String>) {

    fun generateCode(cppModel: CppFile): List<GeneratedFile> {

        val hasConstants = cppModel.members.any { it is CppConstant }
        val hasTypedefs = cppModel.members.any { it is CppUsing }
        val hasNonExternalElements =
            cppModel.members.filterIsInstance<CppExternableElement>().any { !it.isExternal }
        val hasErrorEnums = cppModel.errorEnums.isNotEmpty()
        val hasCode = hasConstants || hasNonExternalElements || hasErrorEnums || hasTypedefs
        if (!hasCode) {
            return emptyList()
        }

        val relativeFilePath = cppModel.filename
        val absoluteHeaderPath = Paths.get(
            pathPrefix,
            PACKAGE_NAME_SPECIFIER_INCLUDE,
            relativeFilePath
        ).toString() + HEADER_FILE_SUFFIX
        val absoluteImplPath = Paths.get(
            pathPrefix,
            PACKAGE_NAME_SPECIFIER_SRC,
            relativeFilePath
        ).toString() + IMPLEMENTATION_FILE_SUFFIX

        // Filter out self-includes
        val includes = cppModel.includes
        includes.removeIf { it.fileName == relativeFilePath + HEADER_FILE_SUFFIX }
        CppLibraryIncludes.filterIncludes(includes, internalNamespace)

        val result = mutableListOf<GeneratedFile>()

        val headerContent = TemplateEngine.render("cpp/CppHeader", cppModel)
        result.add(GeneratedFile(headerContent, absoluteHeaderPath))

        cppModel.headerInclude =
            Include.createInternalInclude(relativeFilePath + HEADER_FILE_SUFFIX)

        val implementationContent = TemplateEngine.render("cpp/CppImplementation", cppModel)
        result.add(GeneratedFile(implementationContent, absoluteImplPath))

        return result
    }

    fun generateHelperHeader(headerName: String) =
        generateHelperHeader(headerName, mapOf("internalNamespace" to internalNamespace))

    fun generateHelperHeader(headerName: String, extraInfo: Any): GeneratedFile {
        val content = TemplateEngine.render("cpp/$headerName", extraInfo)
        val resultFileName = Paths.get(
            pathPrefix,
            PACKAGE_NAME_SPECIFIER_INCLUDE,
            internalNamespace.joinToString("/"),
            headerName
        ).toString() + HEADER_FILE_SUFFIX
        return GeneratedFile(content, resultFileName)
    }

    companion object {
        const val HEADER_FILE_SUFFIX = ".h"
        private const val IMPLEMENTATION_FILE_SUFFIX = ".cpp"
        private const val PACKAGE_NAME_SPECIFIER_INCLUDE = "include"
        private const val PACKAGE_NAME_SPECIFIER_SRC = "src"
    }
}
