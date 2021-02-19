package com.remondis.propertypath.impl;

import java.util.function.Function;

import com.remondis.propertypath.api.AssertApplyStrategy;

public class AssertApplyStrategyImpl<I, O, T, E extends Exception> implements AssertApplyStrategy<I, O, T, E> {

  private GetAndApplyImpl<I, O, T, E> getAndApply;

  public AssertApplyStrategyImpl(GetAndApplyImpl<I, O, T, E> getAndApply) {
    this.getAndApply = getAndApply;
  }

  @Override
  public void andApplyAlways(Function<O, T> mapping) {
    if (getAndApply.isApplyIfPresent()) {
      throw new AssertionError(
          "The getter was expected to always apply the mapping function, but actually the getter is configured to only apply the mapping function if a value is present.");
    }
    assertionErrorIfNullCheckFails(mapping);
  }

  @Override
  public void andApplyIfPresent() {
    if (!getAndApply.isApplyIfPresent()) {
      throw new AssertionError(
          "The getter was expected to always apply the mapping function, but actually the getter is configured to only apply the mapping function if a value is present.");
    }
  }

  /**
   * Throws an {@link AssertionError} if the specified {@link Function} is not null safe.
   *
   * @param function The {@link Function} that is null checked.
   * @throws AssertionError Thrown if the null check fails.
   */
  private static <O, T> void assertionErrorIfNullCheckFails(Function<O, T> function) throws AssertionError {
    try {
      function.apply(null);
    } catch (NullPointerException t) {
      throw new AssertionError("The mapping function was expected to be null safe but actually is not.", t);
    } catch (Throwable t) {
      throw new AssertionError("The mapping function threw an unexpected exception.", t);
    }
  }

}
