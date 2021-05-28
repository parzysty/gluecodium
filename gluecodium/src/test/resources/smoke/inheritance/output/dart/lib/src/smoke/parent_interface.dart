import 'dart:ffi';
import 'package:library/src/_library_context.dart' as __lib;
import 'package:library/src/_native_base.dart' as __lib;
import 'package:library/src/_token_cache.dart' as __lib;
import 'package:library/src/_type_repository.dart' as __lib;
import 'package:library/src/builtin_types__conversion.dart';
import 'package:meta/meta.dart';
abstract class ParentInterface {
  factory ParentInterface(
    void Function() rootMethodLambda,
    String Function() rootPropertyGetLambda,
    void Function(String) rootPropertySetLambda
  ) => ParentInterface$Lambdas(
    rootMethodLambda,
    rootPropertyGetLambda,
    rootPropertySetLambda
  );
  /// Destroys the underlying native object.
  ///
  /// Call this to free memory when you no longer need this instance.
  /// Note that setting the instance to null will not destroy the underlying native object.
  void release() {}
  rootMethod();
  String get rootProperty;
  set rootProperty(String value);
}
// ParentInterface "private" section, not exported.
final _smokeParentinterfaceCopyHandle = __lib.catchArgumentError(() => __lib.nativeLibrary.lookupFunction<
    Pointer<Void> Function(Pointer<Void>),
    Pointer<Void> Function(Pointer<Void>)
  >('library_smoke_ParentInterface_copy_handle'));
final _smokeParentinterfaceReleaseHandle = __lib.catchArgumentError(() => __lib.nativeLibrary.lookupFunction<
    Void Function(Pointer<Void>),
    void Function(Pointer<Void>)
  >('library_smoke_ParentInterface_release_handle'));
final _smokeParentinterfaceCreateProxy = __lib.catchArgumentError(() => __lib.nativeLibrary.lookupFunction<
    Pointer<Void> Function(Uint64, Int32, Pointer, Pointer, Pointer, Pointer),
    Pointer<Void> Function(int, int, Pointer, Pointer, Pointer, Pointer)
  >('library_smoke_ParentInterface_create_proxy'));
final _smokeParentinterfaceGetTypeId = __lib.catchArgumentError(() => __lib.nativeLibrary.lookupFunction<
    Pointer<Void> Function(Pointer<Void>),
    Pointer<Void> Function(Pointer<Void>)
  >('library_smoke_ParentInterface_get_type_id'));
class ParentInterface$Lambdas implements ParentInterface {
  void Function() rootMethodLambda;
  String Function() rootPropertyGetLambda;
  void Function(String) rootPropertySetLambda;
  ParentInterface$Lambdas(
    this.rootMethodLambda,
    this.rootPropertyGetLambda,
    this.rootPropertySetLambda
  );
  @override
  void release() {}
  @override
  rootMethod() =>
    rootMethodLambda();
  @override
  String get rootProperty => rootPropertyGetLambda();
  @override
  set rootProperty(String value) => rootPropertySetLambda(value);
}
class ParentInterface$Impl extends __lib.NativeBase implements ParentInterface {
  ParentInterface$Impl(Pointer<Void> handle) : super(handle);
  @override
  void release() {
    if (handle.address == 0) return;
    __lib.uncacheObject(this);
    __lib.ffiUncacheToken(handle, __lib.LibraryContext.isolateId);
    _smokeParentinterfaceReleaseHandle(handle);
    handle = Pointer<Void>.fromAddress(0);
  }
  @override
  rootMethod() {
    final _rootMethodFfi = __lib.catchArgumentError(() => __lib.nativeLibrary.lookupFunction<Void Function(Pointer<Void>, Int32), void Function(Pointer<Void>, int)>('library_smoke_ParentInterface_rootMethod'));
    final _handle = this.handle;
    final __resultHandle = _rootMethodFfi(_handle, __lib.LibraryContext.isolateId);
    try {
      return (__resultHandle);
    } finally {
    }
  }
  String get rootProperty {
    final _getFfi = __lib.catchArgumentError(() => __lib.nativeLibrary.lookupFunction<Pointer<Void> Function(Pointer<Void>, Int32), Pointer<Void> Function(Pointer<Void>, int)>('library_smoke_ParentInterface_rootProperty_get'));
    final _handle = this.handle;
    final __resultHandle = _getFfi(_handle, __lib.LibraryContext.isolateId);
    try {
      return stringFromFfi(__resultHandle);
    } finally {
      stringReleaseFfiHandle(__resultHandle);
    }
  }
  set rootProperty(String value) {
    final _setFfi = __lib.catchArgumentError(() => __lib.nativeLibrary.lookupFunction<Void Function(Pointer<Void>, Int32, Pointer<Void>), void Function(Pointer<Void>, int, Pointer<Void>)>('library_smoke_ParentInterface_rootProperty_set__String'));
    final _valueHandle = stringToFfi(value);
    final _handle = this.handle;
    final __resultHandle = _setFfi(_handle, __lib.LibraryContext.isolateId, _valueHandle);
    stringReleaseFfiHandle(_valueHandle);
    try {
      return (__resultHandle);
    } finally {
    }
  }
}
int _smokeParentinterfacerootMethodStatic(int _token) {
  try {
    (__lib.instanceCache[_token] as ParentInterface).rootMethod();
  } finally {
  }
  return 0;
}
int _smokeParentinterfacerootPropertyGetStatic(int _token, Pointer<Pointer<Void>> _result) {
  _result.value = stringToFfi((__lib.instanceCache[_token] as ParentInterface).rootProperty);
  return 0;
}
int _smokeParentinterfacerootPropertySetStatic(int _token, Pointer<Void> _value) {
  try {
    (__lib.instanceCache[_token] as ParentInterface).rootProperty =
      stringFromFfi(_value);
  } finally {
    stringReleaseFfiHandle(_value);
  }
  return 0;
}
Pointer<Void> smokeParentinterfaceToFfi(ParentInterface value) {
  if (value is __lib.NativeBase) return _smokeParentinterfaceCopyHandle((value as __lib.NativeBase).handle);
  final result = _smokeParentinterfaceCreateProxy(
    __lib.cacheObject(value),
    __lib.LibraryContext.isolateId,
    __lib.uncacheObjectFfi,
    Pointer.fromFunction<Uint8 Function(Uint64)>(_smokeParentinterfacerootMethodStatic, __lib.unknownError),
    Pointer.fromFunction<Uint8 Function(Uint64, Pointer<Pointer<Void>>)>(_smokeParentinterfacerootPropertyGetStatic, __lib.unknownError),
    Pointer.fromFunction<Uint8 Function(Uint64, Pointer<Void>)>(_smokeParentinterfacerootPropertySetStatic, __lib.unknownError)
  );
  return result;
}
ParentInterface smokeParentinterfaceFromFfi(Pointer<Void> handle) {
  final isolateId = __lib.LibraryContext.isolateId;
  final token = __lib.ffiGetCachedToken(handle, isolateId);
  final instance = __lib.instanceCache[token];
  if (instance is ParentInterface) return instance;
  final _typeIdHandle = _smokeParentinterfaceGetTypeId(handle);
  final factoryConstructor = __lib.typeRepository[stringFromFfi(_typeIdHandle)];
  stringReleaseFfiHandle(_typeIdHandle);
  final _copiedHandle = _smokeParentinterfaceCopyHandle(handle);
  final result = factoryConstructor != null
    ? factoryConstructor(_copiedHandle)
    : ParentInterface$Impl(_copiedHandle);
  __lib.ffiCacheToken(_copiedHandle, isolateId, __lib.cacheObject(result));
  return result;
}
void smokeParentinterfaceReleaseFfiHandle(Pointer<Void> handle) =>
  _smokeParentinterfaceReleaseHandle(handle);
Pointer<Void> smokeParentinterfaceToFfiNullable(ParentInterface? value) =>
  value != null ? smokeParentinterfaceToFfi(value) : Pointer<Void>.fromAddress(0);
ParentInterface? smokeParentinterfaceFromFfiNullable(Pointer<Void> handle) =>
  handle.address != 0 ? smokeParentinterfaceFromFfi(handle) : null;
void smokeParentinterfaceReleaseFfiHandleNullable(Pointer<Void> handle) =>
  _smokeParentinterfaceReleaseHandle(handle);
// End of ParentInterface "private" section.
