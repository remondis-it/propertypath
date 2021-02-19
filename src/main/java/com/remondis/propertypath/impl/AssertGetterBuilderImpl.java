package com.remondis.propertypath.impl;

import com.remondis.propertypath.api.AssertGetterBuilder;
import com.remondis.propertypath.api.Get;
import com.remondis.propertypath.api.GetAndApply;
import com.remondis.propertypath.api.PropertyPath;

public class AssertGetterBuilderImpl<I, O, T, E extends Exception> implements AssertGetterBuilder<I, O, E> {

  private GetImpl<I, O, E> getter;

  public AssertGetterBuilderImpl(Get<I, O, E> getter) {
    checkGetImpl(getter);
    this.getter = (GetImpl<I, O, E>) getter;
  }

  private void checkGetImpl(Get<I, O, E> getter) {
    // This time we are impl-specific.
    if (!(getter instanceof GetImpl)) {
      throw new IllegalArgumentException("The type of getter instance is not supported. Needs to be GetImpl.");
    }
  }

  public AssertGetterBuilderImpl(GetAndApply<I, O, T, E> getter) {
    checkGetAndApply(getter);
    GetAndApplyImpl<I, O, T, E> impl = (GetAndApplyImpl<I, O, T, E>) getter;
    Get<I, O, E> originalGetter = impl.getGetter();
    checkGetImpl(originalGetter);
    this.getter = (GetImpl<I, O, E>) originalGetter;
  }

  private void checkGetAndApply(GetAndApply<I, O, T, E> getter) {
    // This time we are impl-specific.
    if (!(getter instanceof GetAndApplyImpl)) {
      throw new IllegalArgumentException("The type of getter instance is not supported. Needs to be GetAndApplyImpl.");
    }
  }

  @Override
  public void assertGetter(PropertyPath<O, I, E> selector) {
    assertPropertyPath(getter.getTransitiveProperty(), getter.getStartType(), selector);
  }

  static <I, E extends Exception> void assertPropertyPath(TypedTransitiveProperty<I, ?, E> actualProperty,
      Class<I> startType, PropertyPath<?, I, E> selector) throws AssertionError {
    TypedTransitiveProperty<I, ?, E> expectedProperty = GetImpl.buildTransitiveProperty(startType, selector);
    if (!actualProperty.equals(expectedProperty)) {
      throw new AssertionError(
          "The expected propertypath does not match the actual property path of this getter:\nExpected: "
              + expectedProperty.toString(true) + "\n  Actual: " + actualProperty.toString(true));
    }
  }

}
