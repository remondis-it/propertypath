package com.remondis.propertypath.api;

import com.remondis.propertypath.impl.AssertGetterBuilderImpl;

public class AssertGetter {
  public static <I, O, E extends Exception> AssertGetterBuilder<I, O, E> of(Get<I, O, E> getter) {
    return new AssertGetterBuilderImpl<>(getter);
  }
}
