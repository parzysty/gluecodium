// -------------------------------------------------------------------------------------------------
// Copyright (C) 2016-2019 HERE Europe B.V.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
// SPDX-License-Identifier: Apache-2.0
// License-Filename: LICENSE
//
// -------------------------------------------------------------------------------------------------

import "test/Classes_test.dart" as ClassesTests;
import "test/Interfaces_test.dart" as InterfacesTests;
import "test/PlainDataStructures_test.dart" as PlainDataStructuresTests;
import "test/PlainDataStructuresTypeCollection_test.dart" as PlainDataStructuresTypeCollectionTests;
import "test/StaticBooleanMethods_test.dart" as StaticBooleanMethodsTests;
import "test/StaticFloatDoubleMethods_test.dart" as StaticFloatDoubleMethodsTests;
import "test/StaticIntMethods_test.dart" as StaticIntMethodsTests;
import "test/StaticStringMethods_test.dart" as StaticStringMethodsTests;

final _allTests = [
  ClassesTests.main,
  InterfacesTests.main,
  PlainDataStructuresTests.main,
  PlainDataStructuresTypeCollectionTests.main,
  StaticBooleanMethodsTests.main,
  StaticFloatDoubleMethodsTests.main,
  StaticIntMethodsTests.main,
  StaticStringMethodsTests.main
];

void main() {
  _allTests.forEach((testCase) => testCase());
}
