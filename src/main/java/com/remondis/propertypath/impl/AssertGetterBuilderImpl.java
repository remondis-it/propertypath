package com.remondis.propertypath.impl;

import com.remondis.propertypath.api.AssertGetterBuilder;
import com.remondis.propertypath.api.Get;
import com.remondis.propertypath.api.PropertyPath;

public class AssertGetterBuilderImpl<I, O, E extends Exception> implements AssertGetterBuilder<I, O, E> {

  private GetImpl<I, O, E> getter;

  public AssertGetterBuilderImpl(Get<I, O, E> getter) {
    // This time we are impl-specific.
    if (!(getter instanceof GetImpl)) {
      throw new IllegalArgumentException("The type of getter instance is not supported. Needs to be GetImpl.");
    }
    this.getter = (GetImpl<I, O, E>) getter;
  }

  @Override
  public void assertGetter(PropertyPath<O, I, E> selector) {
    TypedTransitiveProperty<I, O, E> actualProperty = getter.getTransitiveProperty();
    TypedTransitiveProperty<I, O, E> expectedProperty = GetImpl.buildTransitiveProperty(getter.getStartType(),
        selector);
    if (!actualProperty.equals(expectedProperty)) {
      throw new AssertionError(
          "The expected propertypath does not match the actual property path of this getter:\nExpected: "
              + expectedProperty.toString(true) + "\n  Actual: " + actualProperty.toString(true));
    }
  }

}
