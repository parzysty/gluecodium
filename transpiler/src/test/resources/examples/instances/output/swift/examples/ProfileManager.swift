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




internal func getRef(_ ref: ProfileManager) -> RefHolder<examples_ProfileManagerRef> {
    return RefHolder<examples_ProfileManagerRef>(ref.c_instance)
}

public class ProfileManager {
    let c_instance : examples_ProfileManagerRef

    public required init?(cProfileManager: examples_ProfileManagerRef) {
        c_instance = cProfileManager
    }

    deinit {
        examples_ProfileManager_release(c_instance)
    }
    public func createProfile(username: String) -> Void {
        return examples_ProfileManager_createProfile(c_instance, username)
    }

    public func changeProfile(username: String) -> String? {
        let result_string_handle = examples_ProfileManager_changeProfile(c_instance, username)
        precondition(result_string_handle.private_pointer != nil, "Out of memory")
        defer {
            std_string_release(result_string_handle)
        }
        return String(data: Data(bytes: std_string_data_get(result_string_handle),
                                 count: Int(std_string_size_get(result_string_handle))), encoding: .utf8)
    }

    public func deleteProfile(username: String) -> Void {
        return examples_ProfileManager_deleteProfile(c_instance, username)
    }

}
