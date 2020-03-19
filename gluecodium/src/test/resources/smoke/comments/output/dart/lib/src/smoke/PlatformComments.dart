import 'package:library/src/BuiltInTypes__conversion.dart';
import 'dart:ffi';
import 'package:ffi/ffi.dart';
import 'package:meta/meta.dart';
import 'package:library/src/_library_init.dart' as __lib;
abstract class PlatformComments {
  void release();
  /// This is some very useless method that cannot have overloads.
  doNothing();
  /// Colors everything in fuchsia.
  doMagic();
  /// This is some very useful method that measures the usefulness of its input or \esc@pe{s}.
  /// @param[input] Very useful parameter that \esc@pe{s}
  /// @return Uselessness [PlatformComments_SomeEnum] of the input
  /// @throws Sometimes it happens.
  bool someMethodWithAllComments(String input);
}
enum PlatformComments_SomeEnum {
    useless,
    useful
}
// PlatformComments_SomeEnum "private" section, not exported.
int smoke_PlatformComments_SomeEnum_toFfi(PlatformComments_SomeEnum value) {
  switch (value) {
  case PlatformComments_SomeEnum.useless:
    return 0;
  break;
  case PlatformComments_SomeEnum.useful:
    return 1;
  break;
  default:
    throw StateError("Invalid enum value $value for PlatformComments_SomeEnum enum.");
  }
}
PlatformComments_SomeEnum smoke_PlatformComments_SomeEnum_fromFfi(int handle) {
  switch (handle) {
  case 0:
    return PlatformComments_SomeEnum.useless;
  break;
  case 1:
    return PlatformComments_SomeEnum.useful;
  break;
  default:
    throw StateError("Invalid numeric value $handle for PlatformComments_SomeEnum enum.");
  }
}
void smoke_PlatformComments_SomeEnum_releaseFfiHandle(int handle) {}
final _smoke_PlatformComments_SomeEnum_create_handle_nullable = __lib.nativeLibrary.lookupFunction<
    Pointer<Void> Function(Uint32),
    Pointer<Void> Function(int)
  >('library_smoke_PlatformComments_SomeEnum_create_handle_nullable');
final _smoke_PlatformComments_SomeEnum_release_handle_nullable = __lib.nativeLibrary.lookupFunction<
    Void Function(Pointer<Void>),
    void Function(Pointer<Void>)
  >('library_smoke_PlatformComments_SomeEnum_release_handle_nullable');
final _smoke_PlatformComments_SomeEnum_get_value_nullable = __lib.nativeLibrary.lookupFunction<
    Uint32 Function(Pointer<Void>),
    int Function(Pointer<Void>)
  >('library_smoke_PlatformComments_SomeEnum_get_value_nullable');
Pointer<Void> smoke_PlatformComments_SomeEnum_toFfi_nullable(PlatformComments_SomeEnum value) {
  if (value == null) return Pointer<Void>.fromAddress(0);
  final _handle = smoke_PlatformComments_SomeEnum_toFfi(value);
  final result = _smoke_PlatformComments_SomeEnum_create_handle_nullable(_handle);
  smoke_PlatformComments_SomeEnum_releaseFfiHandle(_handle);
  return result;
}
PlatformComments_SomeEnum smoke_PlatformComments_SomeEnum_fromFfi_nullable(Pointer<Void> handle) {
  if (handle.address == 0) return null;
  final _handle = _smoke_PlatformComments_SomeEnum_get_value_nullable(handle);
  final result = smoke_PlatformComments_SomeEnum_fromFfi(_handle);
  smoke_PlatformComments_SomeEnum_releaseFfiHandle(_handle);
  return result;
}
void smoke_PlatformComments_SomeEnum_releaseFfiHandle_nullable(Pointer<Void> handle) =>
  _smoke_PlatformComments_SomeEnum_release_handle_nullable(handle);
// End of PlatformComments_SomeEnum "private" section.
/// An exception when something goes wrong.
class PlatformComments_SomethingWrongException implements Exception {
  final PlatformComments_SomeEnum error;
  PlatformComments_SomethingWrongException(this.error);
}
/// This is a.
class PlatformComments_Something {
  String nothing;
  PlatformComments_Something(this.nothing);
}
// PlatformComments_Something "private" section, not exported.
final _smoke_PlatformComments_Something_create_handle = __lib.nativeLibrary.lookupFunction<
    Pointer<Void> Function(Pointer<Void>),
    Pointer<Void> Function(Pointer<Void>)
  >('library_smoke_PlatformComments_Something_create_handle');
final _smoke_PlatformComments_Something_release_handle = __lib.nativeLibrary.lookupFunction<
    Void Function(Pointer<Void>),
    void Function(Pointer<Void>)
  >('library_smoke_PlatformComments_Something_release_handle');
final _smoke_PlatformComments_Something_get_field_nothing = __lib.nativeLibrary.lookupFunction<
    Pointer<Void> Function(Pointer<Void>),
    Pointer<Void> Function(Pointer<Void>)
  >('library_smoke_PlatformComments_Something_get_field_nothing');
Pointer<Void> smoke_PlatformComments_Something_toFfi(PlatformComments_Something value) {
  final _nothing_handle = String_toFfi(value.nothing);
  final _result = _smoke_PlatformComments_Something_create_handle(_nothing_handle);
  String_releaseFfiHandle(_nothing_handle);
  return _result;
}
PlatformComments_Something smoke_PlatformComments_Something_fromFfi(Pointer<Void> handle) {
  final _nothing_handle = _smoke_PlatformComments_Something_get_field_nothing(handle);
  final _result = PlatformComments_Something(
    String_fromFfi(_nothing_handle)
  );
  String_releaseFfiHandle(_nothing_handle);
  return _result;
}
void smoke_PlatformComments_Something_releaseFfiHandle(Pointer<Void> handle) => _smoke_PlatformComments_Something_release_handle(handle);
// Nullable PlatformComments_Something
final _smoke_PlatformComments_Something_create_handle_nullable = __lib.nativeLibrary.lookupFunction<
    Pointer<Void> Function(Pointer<Void>),
    Pointer<Void> Function(Pointer<Void>)
  >('library_smoke_PlatformComments_Something_create_handle_nullable');
final _smoke_PlatformComments_Something_release_handle_nullable = __lib.nativeLibrary.lookupFunction<
    Void Function(Pointer<Void>),
    void Function(Pointer<Void>)
  >('library_smoke_PlatformComments_Something_release_handle_nullable');
final _smoke_PlatformComments_Something_get_value_nullable = __lib.nativeLibrary.lookupFunction<
    Pointer<Void> Function(Pointer<Void>),
    Pointer<Void> Function(Pointer<Void>)
  >('library_smoke_PlatformComments_Something_get_value_nullable');
Pointer<Void> smoke_PlatformComments_Something_toFfi_nullable(PlatformComments_Something value) {
  if (value == null) return Pointer<Void>.fromAddress(0);
  final _handle = smoke_PlatformComments_Something_toFfi(value);
  final result = _smoke_PlatformComments_Something_create_handle_nullable(_handle);
  smoke_PlatformComments_Something_releaseFfiHandle(_handle);
  return result;
}
PlatformComments_Something smoke_PlatformComments_Something_fromFfi_nullable(Pointer<Void> handle) {
  if (handle.address == 0) return null;
  final _handle = _smoke_PlatformComments_Something_get_value_nullable(handle);
  final result = smoke_PlatformComments_Something_fromFfi(_handle);
  smoke_PlatformComments_Something_releaseFfiHandle(_handle);
  return result;
}
void smoke_PlatformComments_Something_releaseFfiHandle_nullable(Pointer<Void> handle) =>
  _smoke_PlatformComments_Something_release_handle_nullable(handle);
// End of PlatformComments_Something "private" section.
// PlatformComments "private" section, not exported.
final _smoke_PlatformComments_copy_handle = __lib.nativeLibrary.lookupFunction<
    Pointer<Void> Function(Pointer<Void>),
    Pointer<Void> Function(Pointer<Void>)
  >('library_smoke_PlatformComments_copy_handle');
final _smoke_PlatformComments_release_handle = __lib.nativeLibrary.lookupFunction<
    Void Function(Pointer<Void>),
    void Function(Pointer<Void>)
  >('library_smoke_PlatformComments_release_handle');
final _someMethodWithAllComments_return_release_handle = __lib.nativeLibrary.lookupFunction<
    Void Function(Pointer<Void>),
    void Function(Pointer<Void>)
  >('library_smoke_PlatformComments_someMethodWithAllComments__String_return_release_handle');
final _someMethodWithAllComments_return_get_result = __lib.nativeLibrary.lookupFunction<
    Uint8 Function(Pointer<Void>),
    int Function(Pointer<Void>)
  >('library_smoke_PlatformComments_someMethodWithAllComments__String_return_get_result');
final _someMethodWithAllComments_return_get_error = __lib.nativeLibrary.lookupFunction<
    Uint32 Function(Pointer<Void>),
    int Function(Pointer<Void>)
  >('library_smoke_PlatformComments_someMethodWithAllComments__String_return_get_error');
final _someMethodWithAllComments_return_has_error = __lib.nativeLibrary.lookupFunction<
    Uint8 Function(Pointer<Void>),
    int Function(Pointer<Void>)
  >('library_smoke_PlatformComments_someMethodWithAllComments__String_return_has_error');
class PlatformComments$Impl implements PlatformComments {
  final Pointer<Void> handle;
  PlatformComments$Impl(this.handle);
  @override
  void release() => _smoke_PlatformComments_release_handle(handle);
  @override
  doNothing() {
    final _doNothing_ffi = __lib.nativeLibrary.lookupFunction<Void Function(Pointer<Void>), void Function(Pointer<Void>)>('library_smoke_PlatformComments_doNothing');
    final _handle = this.handle;
    final __result_handle = _doNothing_ffi(_handle);
    final _result = (__result_handle);
    (__result_handle);
    return _result;
  }
  @override
  doMagic() {
    final _doMagic_ffi = __lib.nativeLibrary.lookupFunction<Void Function(Pointer<Void>), void Function(Pointer<Void>)>('library_smoke_PlatformComments_doMagic');
    final _handle = this.handle;
    final __result_handle = _doMagic_ffi(_handle);
    final _result = (__result_handle);
    (__result_handle);
    return _result;
  }
  @override
  bool someMethodWithAllComments(String input) {
    final _someMethodWithAllComments_ffi = __lib.nativeLibrary.lookupFunction<Pointer<Void> Function(Pointer<Void>, Pointer<Void>), Pointer<Void> Function(Pointer<Void>, Pointer<Void>)>('library_smoke_PlatformComments_someMethodWithAllComments__String');
    final _input_handle = String_toFfi(input);
    final _handle = this.handle;
    final __call_result_handle = _someMethodWithAllComments_ffi(_handle, _input_handle);
    String_releaseFfiHandle(_input_handle);
    if (_someMethodWithAllComments_return_has_error(__call_result_handle) != 0) {
        final __error_handle = _someMethodWithAllComments_return_get_error(__call_result_handle);
        _someMethodWithAllComments_return_release_handle(__call_result_handle);
        final _error_value = smoke_PlatformComments_SomeEnum_fromFfi(__error_handle);
        smoke_PlatformComments_SomeEnum_releaseFfiHandle(__error_handle);
        throw PlatformComments_SomethingWrongException(_error_value);
    }
    final __result_handle = _someMethodWithAllComments_return_get_result(__call_result_handle);
    _someMethodWithAllComments_return_release_handle(__call_result_handle);
    final _result = Boolean_fromFfi(__result_handle);
    Boolean_releaseFfiHandle(__result_handle);
    return _result;
  }
}
Pointer<Void> smoke_PlatformComments_toFfi(PlatformComments value) =>
  _smoke_PlatformComments_copy_handle((value as PlatformComments$Impl).handle);
PlatformComments smoke_PlatformComments_fromFfi(Pointer<Void> handle) =>
  PlatformComments$Impl(_smoke_PlatformComments_copy_handle(handle));
void smoke_PlatformComments_releaseFfiHandle(Pointer<Void> handle) =>
  _smoke_PlatformComments_release_handle(handle);
Pointer<Void> smoke_PlatformComments_toFfi_nullable(PlatformComments value) =>
  value != null ? smoke_PlatformComments_toFfi(value) : Pointer<Void>.fromAddress(0);
PlatformComments smoke_PlatformComments_fromFfi_nullable(Pointer<Void> handle) =>
  handle.address != 0 ? smoke_PlatformComments_fromFfi(handle) : null;
void smoke_PlatformComments_releaseFfiHandle_nullable(Pointer<Void> handle) =>
  _smoke_PlatformComments_release_handle(handle);
// End of PlatformComments "private" section.