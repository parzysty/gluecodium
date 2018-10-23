//
//
// Automatically generated. Do not modify. Your changes will be lost.
import Foundation
internal func getRef(_ ref: Comments?) -> RefHolder {
    return RefHolder(ref?.c_instance ?? 0)
}
/// This is some very useful interface.
public class Comments {
    /// This is some very useful typedef.
    public typealias Usefulness = Bool
    /// This is some very useful map.
    public typealias SomeMap = [String: Comments.Usefulness]
    /// This is some very useful constant.
    public static let veryUseful: Comments.Usefulness = true
    /// This is some very useful attribute.
    public var someAttribute: Comments.Usefulness {
        get {
            return examples_Comments_someAttribute_get(c_instance)
        }
        set {
            return examples_Comments_someAttribute_set(c_instance, newValue)
        }
    }
    let c_instance : _baseRef
    public init?(cComments: _baseRef) {
        guard cComments != 0 else {
            return nil
        }
        c_instance = cComments
    }
    deinit {
        examples_Comments_release(c_instance)
    }
    /// This is some very useful enum.
    public enum SomeEnum : UInt32 {
        /// Not quite useful
        case useless
        /// Somewhat useful
        case useful
    }
    /// This is some very useful struct.
    public struct SomeStruct {
        /// How useful this struct is
        public var someField: Comments.Usefulness
        public init(someField: Comments.Usefulness) {
            self.someField = someField
        }
        internal init?(cSomeStruct: _baseRef) {
            someField = examples_Comments_SomeStruct_someField_get(cSomeStruct)
        }
        internal func convertToCType() -> _baseRef {
            let someField_handle = someField
            return examples_Comments_SomeStruct_create(someField_handle)
        }
    }
    /// This is some very useful method that measures the usefulness of its input.
    /// - Parameter input: Very useful input parameter
    /// - Returns: Usefulness of the input
    public func someMethod(input: String) -> Comments.Usefulness {
        return examples_Comments_someMethod(c_instance, input)
    }
}
extension Comments: NativeBase {
    var c_handle: _baseRef { return c_instance }
}
func convertComments_SomeMapToCType(_ swiftDict: Comments.SomeMap) -> _baseRef {
    let c_handle = examples_Comments_SomeMap_create()
    for (swift_key, swift_value) in swiftDict {
        let c_key = swift_key.convertToCType()
        defer {
            std_string_release(c_key)
        }
        let c_value = swift_value
        examples_Comments_SomeMap_put(c_handle, c_key, c_value)
    }
    return c_handle
}
func convertComments_SomeMapFromCType(_ c_handle: _baseRef) -> Comments.SomeMap {
    var swiftDict: Comments.SomeMap = [:]
    let iterator_handle = examples_Comments_SomeMap_iterator(c_handle)
    while examples_Comments_SomeMap_iterator_is_valid(c_handle, iterator_handle) {
        let c_key = examples_Comments_SomeMap_iterator_key(iterator_handle)
        defer {
            std_string_release(c_key)
        }
        let swift_key = String(data: Data(bytes: std_string_data_get(c_key),
                                            count: Int(std_string_size_get(c_key))),
                                            encoding: .utf8)
        let c_value = examples_Comments_SomeMap_iterator_value(iterator_handle)
        let swift_value = c_value
        swiftDict[swift_key!] = swift_value
        examples_Comments_SomeMap_iterator_increment(iterator_handle)
    }
    examples_Comments_SomeMap_iterator_release(iterator_handle)
    return swiftDict
}
