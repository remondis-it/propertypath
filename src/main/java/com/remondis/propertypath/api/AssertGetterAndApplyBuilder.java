package com.remondis.propertypath.api;

public interface AssertGetterAndApplyBuilder<I, X, O, E extends Exception> {

  /**
   * Works exactly like {@link AssertGetterBuilder#assertGetter(PropertyPath)} but expects the getter to have a
   * transform function
   * specified.
   */
  public void assertGetterWithTransform(PropertyPath<X, I, E> selector);

}
