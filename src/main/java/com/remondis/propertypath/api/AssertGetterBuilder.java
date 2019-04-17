package com.remondis.propertypath.api;

public interface AssertGetterBuilder<I, O, E extends Exception> {

  /**
   * Defines the expected property path and checks that the {@link Get} under test, evaluates the expected property
   * path.
   * The expected property path and the actual property path match, if the invoked get-methods and their arguments
   * equal.
   *
   * @param selector The expected property path.
   */
  public void assertGetter(PropertyPath<O, I, E> selector);
}
