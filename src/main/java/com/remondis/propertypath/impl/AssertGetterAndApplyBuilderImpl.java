package com.remondis.propertypath.impl;

import com.remondis.propertypath.api.AssertApplyStrategy;
import com.remondis.propertypath.api.AssertGetterAndApplyBuilder;
import com.remondis.propertypath.api.Get;
import com.remondis.propertypath.api.GetAndApply;
import com.remondis.propertypath.api.PropertyPath;

public class AssertGetterAndApplyBuilderImpl<I, O, T, E extends Exception>
    implements AssertGetterAndApplyBuilder<I, O, T, E> {

  private GetImpl<I, O, E> getter;
  private GetAndApplyImpl<I, O, T, E> getAndApply;

  public AssertGetterAndApplyBuilderImpl(GetAndApply<I, O, T, E> getter) {
    checkGetAndApply(getter);
    this.getAndApply = (GetAndApplyImpl<I, O, T, E>) getter;
    GetAndApplyImpl<I, O, T, E> impl = (GetAndApplyImpl<I, O, T, E>) getter;
    Get<I, O, E> originalGetter = impl.getGetter();
    checkGetImpl(originalGetter);
    this.getter = (GetImpl<I, O, E>) impl.getGetter();
  }

  private void checkGetImpl(Get<I, O, E> getter) {
    // This time we are impl-specific.
    if (!(getter instanceof GetImpl)) {
      throw new IllegalArgumentException("The type of getter instance is not supported. Needs to be GetImpl.");
    }
  }

  private void checkGetAndApply(GetAndApply<I, O, T, E> getter) {
    // This time we are impl-specific.
    if (!(getter instanceof GetAndApplyImpl)) {
      throw new IllegalArgumentException("The type of getter instance is not supported. Needs to be GetAndApplyImpl.");
    }
  }

  @Override
  public AssertApplyStrategy<I, O, T, E> assertGetter(PropertyPath<O, I, E> selector) {
    AssertGetterBuilderImpl.assertPropertyPath(getter.getTransitiveProperty(), getter.getStartType(), selector);
    return new AssertApplyStrategyImpl(getAndApply);
  }

}
