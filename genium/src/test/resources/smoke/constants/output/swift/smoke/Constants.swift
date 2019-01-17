//
//
// Automatically generated. Do not modify. Your changes will be lost.
import Foundation
public enum StateEnum : UInt32 {
    case off
    case on
}
internal func copyFromCType(_ cValue: UInt32) -> StateEnum {
    return StateEnum(rawValue: cValue)!
}
internal func moveFromCType(_ cValue: UInt32) -> StateEnum {
    return copyFromCType(cValue)
}
internal func copyToCType(_ swiftType: StateEnum) -> PrimitiveHolder<UInt32> {
    return PrimitiveHolder(swiftType.rawValue)
}
internal func moveToCType(_ swiftType: StateEnum) -> PrimitiveHolder<UInt32> {
    return copyToCType(swiftType)
}
public struct Constants {
    public static let boolConstant: Bool = true
    public static let intConstant: Int32 = -11
    public static let uintConstant: UInt32 = 4294967295
    public static let floatConstant: Float = 2.71
    public static let doubleConstant: Double = -3.14
    public static let stringConstant: String = "Foo bar"
    public static let enumConstant: StateEnum = StateEnum.on
}