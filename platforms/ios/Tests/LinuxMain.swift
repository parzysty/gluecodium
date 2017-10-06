import XCTest
@testable import helloTests
@testable import testTests

XCTMain([
    testCase(HelloWorldTests.allTests),
    testCase(HelloWorldInstancesTests.allTests),
    testCase(PlainDataStructuresTests.allTests),
    testCase(PlainDataStructuresFromTypeCollectionTests.allTests),
    testCase(StaticBooleanMethodsTests.allTests),
    testCase(StaticByteArrayMethodsTests.allTests),
    testCase(StaticFloatDoubleMethodsTests.allTests),
    testCase(StaticIntMethodsTests.allTests),
    testCase(StaticStringMethodsTests.allTests),
    testCase(EnumsTests.allTests),
    testCase(TypeDefTests.allTests)
])
