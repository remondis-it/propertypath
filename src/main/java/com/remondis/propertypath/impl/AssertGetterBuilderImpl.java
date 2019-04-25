package com.remondis.propertypath.impl;

import com.remondis.propertypath.api.AssertGetterAndApplyBuilder;
import com.remondis.propertypath.api.AssertGetterBuilder;
import com.remondis.propertypath.api.Get;
import com.remondis.propertypath.api.GetAndApply;
import com.remondis.propertypath.api.PropertyPath;

public class AssertGetterBuilderImpl<I, X, O, E extends Exception>
    implements AssertGetterBuilder<I, O, E>, AssertGetterAndApplyBuilder<I, X, O, E> {

  private GetImpl<I, X, O, E> getter;

  public AssertGetterBuilderImpl(Get<I, O, E> getter) {
    // This time we are impl-specific.
    if (!(getter instanceof GetImpl)) {
      throw new IllegalArgumentException("The type of getter instance is not supported. Needs to be GetImpl.");
    }
    this.getter = (GetImpl<I, X, O, E>) getter;
  }

  public AssertGetterBuilderImpl(GetAndApply<I, X, O, E> getter) {
    // This time we are impl-specific.
    if (!(getter instanceof GetImpl)) {
      throw new IllegalArgumentException("The type of getter instance is not supported. Needs to be GetImpl.");
    }
    this.getter = (GetImpl<I, X, O, E>) getter;
  }

  @Override
  public void assertGetter(PropertyPath<O, I, E> selector) {
    assertPropertyPath(selector);
  }

  private void assertPropertyPath(PropertyPath<?, I, E> selector) throws AssertionError {
    TypedTransitiveProperty<I, ?, E> actualProperty = getter.getTransitiveProperty();
    TypedTransitiveProperty<I, ?, E> expectedProperty = GetImpl.buildTransitiveProperty(getter.getStartType(),
        selector);
    if (!actualProperty.equals(expectedProperty)) {
      throw new AssertionError(
          "The expected propertypath does not match the actual property path of this getter:\nExpected: "
              + expectedProperty.toString(true) + "\n  Actual: " + actualProperty.toString(true));
    }
  }

  @Override
  public void assertGetterWithTransform(PropertyPath<X, I, E> selector) {
    assertPropertyPath(selector);

    if (!getter.hasTransformFunction()) {
      throw new AssertionError(
          "The getter was expected to have a transformation function specified, but none was set.");
    }
  }

}
