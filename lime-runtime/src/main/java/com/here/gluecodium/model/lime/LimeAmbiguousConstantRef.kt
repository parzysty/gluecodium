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

package com.here.gluecodium.model.lime

/**
 * An ambiguous reference to an element, either an enumerator or a constant, represented by `relativePath` constructor
 * parameter. The ambiguity is resolved through the [LimeAmbiguityResolver].
 *
 * The resolution logic is "lazy": if it succeeds on the first call then the result is stored and the
 * stored result is used on subsequent calls instead.
 */
class LimeAmbiguousConstantRef(
    private val relativePath: List<String>,
    private val parentPaths: List<LimePath>,
    private val imports: List<LimePath>,
    referenceMap: Map<String, LimeElement>
) : LimeConstantRef() {

    override val element by lazy {
        LimeAmbiguityResolver.resolve<LimeNamedElement>(relativePath, parentPaths, imports, referenceMap)
    }

    override val typeRef by lazy {
        when (val limeElement = element) {
            is LimeConstant -> limeElement.typeRef
            else -> LimeLazyTypeRef(limeElement.path.parent.toString(), referenceMap)
        }
    }

    override fun remap(referenceMap: Map<String, LimeElement>) =
        LimeAmbiguousConstantRef(relativePath, parentPaths, imports, referenceMap)
}
