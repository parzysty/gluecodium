import 'dart:ffi';
import 'package:ffi/ffi.dart';
import 'package:meta/meta.dart';
/// @nodoc
class NativeBase {
  @protected
  Pointer<Void> handle;
  NativeBase(this.handle);
}