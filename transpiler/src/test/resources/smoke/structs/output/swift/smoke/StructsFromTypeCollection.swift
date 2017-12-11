//
// Copyright (C) 2017 HERE Global B.V. and/or its affiliated companies. All rights reserved.
//
// This software, including documentation, is protected by copyright controlled by
// HERE Global B.V. All rights are reserved. Copying, including reproducing, storing,
// adapting or translating, any or all of this material requires the prior written
// consent of HERE Global B.V. This material also contains confidential information,
// which may not be disclosed to others without prior written consent of HERE Global B.V.
//
// Automatically generated. Do not modify. Your changes will be lost.

import Foundation

internal func getRef(_ ref: StructsFromTypeCollection) -> RefHolder<smoke_StructsFromTypeCollectionRef> {
    return RefHolder<smoke_StructsFromTypeCollectionRef>(ref.c_instance)
}

public class StructsFromTypeCollection {
    let c_instance : smoke_StructsFromTypeCollectionRef
    public required init?(cStructsFromTypeCollection: smoke_StructsFromTypeCollectionRef) {
        c_instance = cStructsFromTypeCollection
    }
    deinit {
        smoke_StructsFromTypeCollection_release(c_instance)
    }

    public static func createPoint(x: Double, y: Double) -> Point? {
        let cResult = smoke_StructsFromTypeCollection_createPoint(x, y)
        precondition(cResult.private_pointer != nil, "Out of memory")

        defer {
            smoke_TypeCollection_Point_release(cResult)
        }

        return Point(cPoint: cResult)
    }

    public static func swapPointCoordinates(input: Point) -> Point? {
        let inputHandle = input.convertToCType()
        defer {
            smoke_TypeCollection_Point_release(inputHandle)
        }
        let cResult = smoke_StructsFromTypeCollection_swapPointCoordinates(inputHandle)
        precondition(cResult.private_pointer != nil, "Out of memory")

        defer {
            smoke_TypeCollection_Point_release(cResult)
        }

        return Point(cPoint: cResult)
    }

    public static func createLine(pointA: Point, pointB: Point) -> Line? {
        let pointAHandle = pointA.convertToCType()
        defer {
            smoke_TypeCollection_Point_release(pointAHandle)
        }
        let pointBHandle = pointB.convertToCType()
        defer {
            smoke_TypeCollection_Point_release(pointBHandle)
        }
        let cResult = smoke_StructsFromTypeCollection_createLine(pointAHandle, pointBHandle)
        precondition(cResult.private_pointer != nil, "Out of memory")

        defer {
            smoke_TypeCollection_Line_release(cResult)
        }

        return Line(cLine: cResult)
    }

    public static func createColoredLine(line: Line, color: Color) -> ColoredLine? {
        let lineHandle = line.convertToCType()
        defer {
            smoke_TypeCollection_Line_release(lineHandle)
        }
        let colorHandle = color.convertToCType()
        defer {
            smoke_TypeCollection_Color_release(colorHandle)
        }
        let cResult = smoke_StructsFromTypeCollection_createColoredLine(lineHandle, colorHandle)
        precondition(cResult.private_pointer != nil, "Out of memory")

        defer {
            smoke_TypeCollection_ColoredLine_release(cResult)
        }

        return ColoredLine(cColoredLine: cResult)
    }

    public static func modifyAllTypesStruct(input: AllTypesStruct) -> AllTypesStruct? {
        let inputHandle = input.convertToCType()
        defer {
            smoke_TypeCollection_AllTypesStruct_release(inputHandle)
        }
        let cResult = smoke_StructsFromTypeCollection_modifyAllTypesStruct(inputHandle)
        precondition(cResult.private_pointer != nil, "Out of memory")

        defer {
            smoke_TypeCollection_AllTypesStruct_release(cResult)
        }

        return AllTypesStruct(cAllTypesStruct: cResult)
    }

}
