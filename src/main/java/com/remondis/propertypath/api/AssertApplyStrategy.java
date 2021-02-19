package com.remondis.propertypath.api;

import java.util.function.Function;

/**
 * Defines the interface to specify asserts on the if-present-strategy of mapping functions.
 *
 * @param <I> The input object type
 * @param <O> The property type.
 * @param <T> The type after applying the mapping function.
 * @param <E> The exception type that maybe thrown by the property path.
 */
public interface AssertApplyStrategy<I, O, T, E extends Exception> {

  /**
   * Expects the getter to always apply the mapping function. Therefore the mapping function is tested against a
   * <code>null</code> value. The mapping function must not throw an exception if a <code>null</code> value is passed to
   * it.
   *
   * @param mapping The mapping function to test against <code>null</code>.
   */
  public void andApplyAlways(Function<O, T> mapping);

  /**
   * Expects the getter to only apply the mapping function if the property path returns a non-null value.
   */
  public void andApplyIfPresent();

}
