//
//
// Automatically generated. Do not modify. Your changes will be lost.
#include "cbridge/include/smoke/cbridge_GenericTypesWithGenericTypes.h"
#include "cbridge_internal/include/BaseHandleImpl.h"
#include "genium/Optional.h"
#include "smoke/GenericTypesWithGenericTypes.h"
#include <memory>
#include <new>
#include <unordered_map>
#include <unordered_set>
#include <vector>
void smoke_GenericTypesWithGenericTypes_release_handle(_baseRef handle) {
    delete get_pointer<std::shared_ptr<::smoke::GenericTypesWithGenericTypes>>(handle);
}
_baseRef smoke_GenericTypesWithGenericTypes_copy_handle(_baseRef handle) {
    return handle
        ? reinterpret_cast<_baseRef>(checked_pointer_copy(*get_pointer<std::shared_ptr<::smoke::GenericTypesWithGenericTypes>>(handle)))
        : 0;
}
_baseRef smoke_GenericTypesWithGenericTypes_methodWithListOfLists(_baseRef _instance, _baseRef input) {
    return Conversion<std::vector<std::vector<int32_t>>>::toBaseRef(get_pointer<std::shared_ptr<::smoke::GenericTypesWithGenericTypes>>(_instance)->get()->method_with_list_of_lists(Conversion<std::vector<std::vector<int32_t>>>::toCpp(input)))
;
}
_baseRef smoke_GenericTypesWithGenericTypes_methodWithMapOfMaps(_baseRef _instance, _baseRef input) {
    return Conversion<std::unordered_map<std::unordered_map<int32_t, bool>, bool, genium::hash<std::unordered_map<int32_t, bool>>>>::toBaseRef(get_pointer<std::shared_ptr<::smoke::GenericTypesWithGenericTypes>>(_instance)->get()->method_with_map_of_maps(Conversion<std::unordered_map<int32_t, std::unordered_map<int32_t, bool>>>::toCpp(input)))
;
}
_baseRef smoke_GenericTypesWithGenericTypes_methodWithSetOfSets(_baseRef _instance, _baseRef input) {
    return Conversion<std::unordered_set<std::unordered_set<int32_t>, genium::hash<std::unordered_set<int32_t>>>>::toBaseRef(get_pointer<std::shared_ptr<::smoke::GenericTypesWithGenericTypes>>(_instance)->get()->method_with_set_of_sets(Conversion<std::unordered_set<std::unordered_set<int32_t>, genium::hash<std::unordered_set<int32_t>>>>::toCpp(input)))
;
}
_baseRef smoke_GenericTypesWithGenericTypes_methodWithListAndMap(_baseRef _instance, _baseRef input) {
    return Conversion<std::unordered_map<int32_t, std::vector<int32_t>>>::toBaseRef(get_pointer<std::shared_ptr<::smoke::GenericTypesWithGenericTypes>>(_instance)->get()->method_with_list_and_map(Conversion<std::vector<std::unordered_map<int32_t, bool>>>::toCpp(input)))
;
}
_baseRef smoke_GenericTypesWithGenericTypes_methodWithListAndSet(_baseRef _instance, _baseRef input) {
    return Conversion<std::unordered_set<std::vector<int32_t>, genium::hash<std::vector<int32_t>>>>::toBaseRef(get_pointer<std::shared_ptr<::smoke::GenericTypesWithGenericTypes>>(_instance)->get()->method_with_list_and_set(Conversion<std::vector<std::unordered_set<int32_t>>>::toCpp(input)))
;
}
_baseRef smoke_GenericTypesWithGenericTypes_methodWithMapAndSet(_baseRef _instance, _baseRef input) {
    return Conversion<std::unordered_set<std::unordered_map<int32_t, bool>, genium::hash<std::unordered_map<int32_t, bool>>>>::toBaseRef(get_pointer<std::shared_ptr<::smoke::GenericTypesWithGenericTypes>>(_instance)->get()->method_with_map_and_set(Conversion<std::unordered_map<int32_t, std::unordered_set<int32_t>>>::toCpp(input)))
;
}
_baseRef smoke_GenericTypesWithGenericTypes_methodWithMapGenericKeys(_baseRef _instance, _baseRef input) {
    return Conversion<std::unordered_map<std::vector<int32_t>, bool, genium::hash<std::vector<int32_t>>>>::toBaseRef(get_pointer<std::shared_ptr<::smoke::GenericTypesWithGenericTypes>>(_instance)->get()->method_with_map_generic_keys(Conversion<std::unordered_map<std::unordered_set<int32_t>, bool, genium::hash<std::unordered_set<int32_t>>>>::toCpp(input)))
;
}
