package com.remondis.propertypath.api;

/**
 * Defines the interface to specify asserts on {@link GetAndApply} getters.
 *
 * @param <I> The input object type
 * @param <O> The property type.
 * @param <T> The type after applying the mapping function.
 * @param <E> The exception type that maybe thrown by the property path.
 */
public interface AssertGetterAndApplyBuilder<I, O, T, E extends Exception> {

  /**
   * Defines the expected property path and checks that the getter under test, evaluates the expected property
   * path and the mapping function that is applied to the result has the same if-present-strategy.
   *
   * The expected property path and the actual property path match, if the invoked get-methods and their arguments
   * equal as well as the same if-present-strategy was configured for the mapping function.
   *
   * @param selector The expected property path.
   * @return Returns a new {@link AssertApplyStrategy} to assert the if-presen-strategy for the mapping function.
   */
  public AssertApplyStrategy<I, O, T, E> assertGetter(PropertyPath<O, I, E> selector);

}
