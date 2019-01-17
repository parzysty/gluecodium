//
//
// Automatically generated. Do not modify. Your changes will be lost.
import Foundation
internal func getRef(_ ref: TypeDefs?, owning: Bool = true) -> RefHolder {
    guard let c_handle = ref?.c_instance else {
        return RefHolder(0)
    }
    let handle_copy = smoke_TypeDefs_copy_handle(c_handle)
    return owning
        ? RefHolder(ref: handle_copy, release: smoke_TypeDefs_release_handle)
        : RefHolder(handle_copy)
}
public class TypeDefs {
    public typealias NestedIntTypeDef = TypeDefs.PrimitiveTypeDef
    public typealias PrimitiveTypeDef = Double
    public typealias ComplexTypeDef = CollectionOf<TypeDefs.TestStruct>
    public typealias TestStructTypeDef = TypeDefs.TestStruct
    public typealias NestedStructTypeDef = TypeDefs.TestStructTypeDef
    public var primitiveTypeAttribute: CollectionOf<TypeDefs.PrimitiveTypeDef> {
        get {
            return moveFromCType(smoke_TypeDefs_primitiveTypeAttribute_get(self.c_instance))
        }
        set {
                let c_newValue = moveToCType(newValue)
            return moveFromCType(smoke_TypeDefs_primitiveTypeAttribute_set(self.c_instance, c_newValue.ref))
        }
    }
    let c_instance : _baseRef
    init(cTypeDefs: _baseRef) {
        guard cTypeDefs != 0 else {
            fatalError("Nullptr value is not supported for initializers")
        }
        c_instance = cTypeDefs
    }
    deinit {
        smoke_TypeDefs_release_handle(c_instance)
    }
    public struct StructHavingAliasFieldDefinedBelow {
        public var field: TypeDefs.PrimitiveTypeDef
        public init(field: TypeDefs.PrimitiveTypeDef) {
            self.field = field
        }
        internal init(cHandle: _baseRef) {
            field = moveFromCType(smoke_TypeDefs_StructHavingAliasFieldDefinedBelow_field_get(cHandle))
        }
        internal func convertToCType() -> _baseRef {
            let c_field = moveToCType(field)
            return smoke_TypeDefs_StructHavingAliasFieldDefinedBelow_create_handle(c_field.ref)
        }
    }
    public struct TestStruct {
        public var something: String
        public init(something: String) {
            self.something = something
        }
        internal init(cHandle: _baseRef) {
            something = moveFromCType(smoke_TypeDefs_TestStruct_something_get(cHandle))
        }
        internal func convertToCType() -> _baseRef {
            let c_something = moveToCType(something)
            return smoke_TypeDefs_TestStruct_create_handle(c_something.ref)
        }
    }
    public static func methodWithPrimitiveTypeDef(input: TypeDefs.PrimitiveTypeDef) -> TypeDefs.PrimitiveTypeDef {
            let c_input = moveToCType(input)
        return moveFromCType(smoke_TypeDefs_methodWithPrimitiveTypeDef(c_input.ref))
    }
    public static func methodWithComplexTypeDef<Tinput: Collection>(input: Tinput) -> TypeDefs.ComplexTypeDef where Tinput.Element == TypeDefs.TestStruct {
            let c_input = moveToCType(input)
        return moveFromCType(smoke_TypeDefs_methodWithComplexTypeDef(c_input.ref))
    }
    public static func returnNestedIntTypeDef(input: TypeDefs.NestedIntTypeDef) -> TypeDefs.NestedIntTypeDef {
            let c_input = moveToCType(input)
        return moveFromCType(smoke_TypeDefs_returnNestedIntTypeDef(c_input.ref))
    }
    public static func returnTestStructTypeDef(input: TypeDefs.TestStructTypeDef) -> TypeDefs.TestStructTypeDef {
            let c_input = moveToCType(input)
        return moveFromCType(smoke_TypeDefs_returnTestStructTypeDef(c_input.ref))
    }
    public static func returnNestedStructTypeDef(input: TypeDefs.NestedStructTypeDef) -> TypeDefs.NestedStructTypeDef {
            let c_input = moveToCType(input)
        return moveFromCType(smoke_TypeDefs_returnNestedStructTypeDef(c_input.ref))
    }
    public static func returnTypeDefPointFromTypeCollection(input: PointTypeDef) -> PointTypeDef {
            let c_input = moveToCType(input)
        return moveFromCType(smoke_TypeDefs_returnTypeDefPointFromTypeCollection(c_input.ref))
    }
}
extension TypeDefs: NativeBase {
    var c_handle: _baseRef { return c_instance }
}
internal func TypeDefscopyFromCType(_ handle: _baseRef) -> TypeDefs {
    return TypeDefs(cTypeDefs: handle)
}
internal func TypeDefsmoveFromCType(_ handle: _baseRef) -> TypeDefs {
    return TypeDefscopyFromCType(handle)
}
internal func TypeDefscopyFromCType(_ handle: _baseRef) -> TypeDefs? {
    guard handle != 0 else {
        return nil
    }
    return TypeDefsmoveFromCType(handle) as TypeDefs
}
internal func TypeDefsmoveFromCType(_ handle: _baseRef) -> TypeDefs? {
    return TypeDefscopyFromCType(handle)
}
internal func copyToCType(_ swiftClass: TypeDefs) -> RefHolder {
    return getRef(swiftClass, owning: false)
}
internal func moveToCType(_ swiftClass: TypeDefs) -> RefHolder {
    return getRef(swiftClass, owning: true)
}
internal func copyToCType(_ swiftClass: TypeDefs?) -> RefHolder {
    return getRef(swiftClass, owning: false)
}
internal func moveToCType(_ swiftClass: TypeDefs?) -> RefHolder {
    return getRef(swiftClass, owning: true)
}
internal func copyFromCType(_ handle: _baseRef) -> TypeDefs.StructHavingAliasFieldDefinedBelow {
    return TypeDefs.StructHavingAliasFieldDefinedBelow(cHandle: handle)
}
internal func moveFromCType(_ handle: _baseRef) -> TypeDefs.StructHavingAliasFieldDefinedBelow {
    defer {
        smoke_TypeDefs_StructHavingAliasFieldDefinedBelow_release_handle(handle)
    }
    return copyFromCType(handle)
}
internal func copyToCType(_ swiftType: TypeDefs.StructHavingAliasFieldDefinedBelow) -> RefHolder {
    return RefHolder(swiftType.convertToCType())
}
internal func moveToCType(_ swiftType: TypeDefs.StructHavingAliasFieldDefinedBelow) -> RefHolder {
    return RefHolder(ref: copyToCType(swiftType).ref, release: smoke_TypeDefs_StructHavingAliasFieldDefinedBelow_release_handle)
}
internal func copyFromCType(_ handle: _baseRef) -> TypeDefs.TestStruct {
    return TypeDefs.TestStruct(cHandle: handle)
}
internal func moveFromCType(_ handle: _baseRef) -> TypeDefs.TestStruct {
    defer {
        smoke_TypeDefs_TestStruct_release_handle(handle)
    }
    return copyFromCType(handle)
}
internal func copyToCType(_ swiftType: TypeDefs.TestStruct) -> RefHolder {
    return RefHolder(swiftType.convertToCType())
}
internal func moveToCType(_ swiftType: TypeDefs.TestStruct) -> RefHolder {
    return RefHolder(ref: copyToCType(swiftType).ref, release: smoke_TypeDefs_TestStruct_release_handle)
}