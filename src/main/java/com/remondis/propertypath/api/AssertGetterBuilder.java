package com.remondis.propertypath.api;

public interface AssertGetterBuilder<I, O, E extends Exception> {

  /**
   * Checks that the {@link Get} under test, evaluates the same property path as specified.
   *
   * @param selector The expected property path.
   */
  public void assertGetter(PropertyPathWithException<O, I, E> selector);
}
