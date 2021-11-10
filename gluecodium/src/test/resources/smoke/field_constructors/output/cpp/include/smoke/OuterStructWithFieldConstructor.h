// -------------------------------------------------------------------------------------------------
//
//
// -------------------------------------------------------------------------------------------------
#pragma once
#include "gluecodium/ExportGluecodiumCpp.h"
namespace smoke {
struct _GLUECODIUM_CPP_EXPORT OuterStructWithFieldConstructor {
    struct _GLUECODIUM_CPP_EXPORT InnerStructWithDefaults {
        double inner_struct_field = 1.0;
        InnerStructWithDefaults( );
        InnerStructWithDefaults( double inner_struct_field );
    };
    ::smoke::OuterStructWithFieldConstructor::InnerStructWithDefaults outer_struct_field;
    OuterStructWithFieldConstructor( );
    OuterStructWithFieldConstructor( ::smoke::OuterStructWithFieldConstructor::InnerStructWithDefaults outer_struct_field );
};
}
