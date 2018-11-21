//
//
// Automatically generated. Do not modify. Your changes will be lost.
import Foundation
internal func getRef(_ ref: ListenersWithReturnValues?, owning: Bool = true) -> RefHolder {
    guard let reference = ref else {
        return RefHolder(0)
    }
    if let instanceReference = reference as? NativeBase {
        return RefHolder(instanceReference.c_handle)
    }
    var functions = smoke_ListenersWithReturnValues_FunctionTable()
    functions.swift_pointer = Unmanaged<AnyObject>.passRetained(reference).toOpaque()
    functions.release = {swift_class_pointer in
        if let swift_class = swift_class_pointer {
            Unmanaged<AnyObject>.fromOpaque(swift_class).release()
        }
    }
    functions.smoke_ListenersWithReturnValues_fetchData_double = {(swift_class_pointer) in
        let swift_class = Unmanaged<AnyObject>.fromOpaque(swift_class_pointer!).takeUnretainedValue() as! ListenersWithReturnValues
        return swift_class.fetchData()
    }
    functions.smoke_ListenersWithReturnValues_fetchData_string = {(swift_class_pointer) in
        let swift_class = Unmanaged<AnyObject>.fromOpaque(swift_class_pointer!).takeUnretainedValue() as! ListenersWithReturnValues
        return swift_class.fetchData().convertToCType()
    }
    functions.smoke_ListenersWithReturnValues_fetchData_Struct = {(swift_class_pointer) in
        let swift_class = Unmanaged<AnyObject>.fromOpaque(swift_class_pointer!).takeUnretainedValue() as! ListenersWithReturnValues
        return swift_class.fetchData().convertToCType()
    }
    functions.smoke_ListenersWithReturnValues_fetchData_enum = {(swift_class_pointer) in
        let swift_class = Unmanaged<AnyObject>.fromOpaque(swift_class_pointer!).takeUnretainedValue() as! ListenersWithReturnValues
        return swift_class.fetchData().rawValue
    }
    functions.smoke_ListenersWithReturnValues_fetchData_Array = {(swift_class_pointer) in
        let swift_class = Unmanaged<AnyObject>.fromOpaque(swift_class_pointer!).takeUnretainedValue() as! ListenersWithReturnValues
        return swift_class.fetchData().c_conversion().c_type
    }
    functions.smoke_ListenersWithReturnValues_fetchData_Map = {(swift_class_pointer) in
        let swift_class = Unmanaged<AnyObject>.fromOpaque(swift_class_pointer!).takeUnretainedValue() as! ListenersWithReturnValues
        return convertListenersWithReturnValues_StringToDoubleToCType(swift_class.fetchData())
    }
    functions.smoke_ListenersWithReturnValues_fetchData_instance = {(swift_class_pointer) in
        let swift_class = Unmanaged<AnyObject>.fromOpaque(swift_class_pointer!).takeUnretainedValue() as! ListenersWithReturnValues
        return getRef(swift_class.fetchData()!, owning: false).ref
    }
    let proxy = smoke_ListenersWithReturnValues_createProxy(functions)
    return owning ? RefHolder(ref: proxy, release: smoke_ListenersWithReturnValues_release) : RefHolder(proxy)
}
public protocol ListenersWithReturnValues : AnyObject {
    typealias StringToDouble = [String: Double]
    func fetchData() -> Double
    func fetchData() -> String
    func fetchData() -> ResultStruct
    func fetchData() -> ResultEnum
    func fetchData() -> CollectionOf<Double>
    func fetchData() -> ListenersWithReturnValues.StringToDouble
    func fetchData() -> CalculationResult?
}
internal class _ListenersWithReturnValues: ListenersWithReturnValues {
    let c_instance : _baseRef
    init?(cListenersWithReturnValues: _baseRef) {
        guard cListenersWithReturnValues != 0 else {
            return nil
        }
        c_instance = cListenersWithReturnValues
    }
    deinit {
        smoke_ListenersWithReturnValues_release(c_instance)
    }
    public func fetchData() -> Double {
        return smoke_ListenersWithReturnValues_fetchData_double(c_instance)
    }
    public func fetchData() -> String {
        let result_string_handle = smoke_ListenersWithReturnValues_fetchData_string(c_instance)
        defer {
            std_string_release(result_string_handle)
        }
        return String(data: Data(bytes: std_string_data_get(result_string_handle),
                                 count: Int(std_string_size_get(result_string_handle))), encoding: .utf8)!
    }
    public func fetchData() -> ResultStruct {
        let cResult = smoke_ListenersWithReturnValues_fetchData_Struct(c_instance)
        defer {
            smoke_ListenersWithReturnValues_ResultStruct_release(cResult)
        }
        return ResultStruct(cResultStruct: cResult)
    }
    public func fetchData() -> ResultEnum {
        let cResult = smoke_ListenersWithReturnValues_fetchData_enum(c_instance)
        return ResultEnum(rawValue: cResult)!
    }
    public func fetchData() -> CollectionOf<Double> {
        let result_handle = smoke_ListenersWithReturnValues_fetchData_Array(c_instance)
        return DoubleList(result_handle)
    }
    public func fetchData() -> ListenersWithReturnValues.StringToDouble {
        let result_handle = smoke_ListenersWithReturnValues_fetchData_Map(c_instance)
        defer {
            smoke_ListenersWithReturnValues_StringToDouble_release(result_handle)
        }
        return convertListenersWithReturnValues_StringToDoubleFromCType(result_handle)
    }
    public func fetchData() -> CalculationResult? {
        let cResult = smoke_ListenersWithReturnValues_fetchData_instance(c_instance)
        if let swift_pointer = smoke_CalculationResult_get_swift_object_from_cache(cResult),
                let re_constructed = Unmanaged<AnyObject>.fromOpaque(swift_pointer).takeUnretainedValue() as? CalculationResult {
            return re_constructed
        }
        return _CalculationResult(cCalculationResult: cResult)
    }
}
extension _ListenersWithReturnValues: NativeBase {
    var c_handle: _baseRef { return c_instance }
}
public enum ResultEnum : UInt32 {
    case none
    case result
}
public struct ResultStruct {
    public var result: Double
    public init(result: Double) {
        self.result = result
    }
    internal init(cResultStruct: _baseRef) {
        result = smoke_ListenersWithReturnValues_ResultStruct_result_get(cResultStruct)
    }
    internal func convertToCType() -> _baseRef {
        let result_handle = result
        return smoke_ListenersWithReturnValues_ResultStruct_create(result_handle)
    }
}
func convertListenersWithReturnValues_StringToDoubleToCType(_ swiftDict: ListenersWithReturnValues.StringToDouble) -> _baseRef {
    let c_handle = smoke_ListenersWithReturnValues_StringToDouble_create()
    for (swift_key, swift_value) in swiftDict {
        let c_key = swift_key.convertToCType()
        defer {
            std_string_release(c_key)
        }
        let c_value = swift_value
        smoke_ListenersWithReturnValues_StringToDouble_put(c_handle, c_key, c_value)
    }
    return c_handle
}
func convertListenersWithReturnValues_StringToDoubleFromCType(_ c_handle: _baseRef) -> ListenersWithReturnValues.StringToDouble {
    var swiftDict: ListenersWithReturnValues.StringToDouble = [:]
    let iterator_handle = smoke_ListenersWithReturnValues_StringToDouble_iterator(c_handle)
    while smoke_ListenersWithReturnValues_StringToDouble_iterator_is_valid(c_handle, iterator_handle) {
        let c_key = smoke_ListenersWithReturnValues_StringToDouble_iterator_key(iterator_handle)
        defer {
            std_string_release(c_key)
        }
        let swift_key = String(data: Data(bytes: std_string_data_get(c_key),
                                            count: Int(std_string_size_get(c_key))),
                                            encoding: .utf8)
        let c_value = smoke_ListenersWithReturnValues_StringToDouble_iterator_value(iterator_handle)
        let swift_value = c_value
        swiftDict[swift_key!] = swift_value
        smoke_ListenersWithReturnValues_StringToDouble_iterator_increment(iterator_handle)
    }
    smoke_ListenersWithReturnValues_StringToDouble_iterator_release(iterator_handle)
    return swiftDict
}