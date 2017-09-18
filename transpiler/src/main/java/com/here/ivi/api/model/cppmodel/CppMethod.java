/*
 * Copyright (C) 2017 HERE Global B.V. and its affiliate(s). All rights reserved.
 *
 * This software, including documentation, is protected by copyright controlled by
 * HERE Global B.V. All rights are reserved. Copying, including reproducing, storing,
 * adapting or translating, any or all of this material requires the prior written
 * consent of HERE Global B.V. This material also contains confidential information,
 * which may not be disclosed to others without prior written consent of HERE Global B.V.
 *
 */

package com.here.ivi.api.model.cppmodel;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public final class CppMethod extends CppElementWithIncludes {

  public final CppTypeRef returnType;
  public final Set<Specifier> specifiers;
  public final Set<Qualifier> qualifiers;
  public final List<CppParameter> parameters;

  public enum Specifier {
    EXPLICIT("explicit"),
    INLINE("inline"),
    STATIC("static"),
    VIRTUAL("virtual");

    private final String text;

    Specifier(final String text) {
      this.text = text;
    }

    @Override
    public String toString() {
      return text;
    }
  }

  public enum Qualifier {
    CONST("const"),
    OVERRIDE("override"),
    PURE_VIRTUAL("= 0");

    private final String text;

    Qualifier(final String text) {
      this.text = text;
    }

    @Override
    public String toString() {
      return text;
    }
  }

  private CppMethod(
      final String name,
      final String methodComment,
      final CppTypeRef returnType,
      final Set<Specifier> specifiers,
      final Set<Qualifier> qualifiers,
      final List<CppParameter> parameters) {
    super(name);
    this.comment = methodComment;
    this.returnType = returnType;
    this.specifiers = specifiers;
    this.qualifiers = qualifiers;
    this.parameters = parameters;
  }

  @SuppressWarnings("PMD.AvoidFieldNameMatchingMethodName")
  public static class Builder {
    private final String name;
    private String methodComment;
    private CppTypeRef returnType = CppPrimitiveTypeRef.VOID;

    private final Set<Specifier> specifiers = EnumSet.noneOf(Specifier.class);
    private final Set<Qualifier> qualifiers = EnumSet.noneOf(Qualifier.class);
    private final List<CppParameter> parameters = new ArrayList<>();

    public Builder(String name) {
      this.name = name;
    }

    public Builder comment(String comment) {
      this.methodComment = comment;
      return this;
    }

    public Builder returnType(CppTypeRef type) {
      this.returnType = type;
      return this;
    }

    public Builder specifier(Specifier theSpecifier) {
      this.specifiers.add(theSpecifier);
      return this;
    }

    public Builder qualifier(Qualifier theQualifier) {
      this.qualifiers.add(theQualifier);
      return this;
    }

    public Builder parameter(CppParameter parameter) {
      this.parameters.add(parameter);
      return this;
    }

    public CppMethod build() {
      return new CppMethod(
          this.name,
          this.methodComment,
          this.returnType,
          this.specifiers,
          this.qualifiers,
          this.parameters);
    }
  }

  @Override
  public Stream<? extends CppElement> stream() {
    return Stream.concat(Stream.of(returnType), parameters.stream());
  }
}
