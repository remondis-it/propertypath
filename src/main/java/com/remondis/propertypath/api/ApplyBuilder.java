package com.remondis.propertypath.api;

import java.util.function.Function;

/**
 * This is the builder stage for specifying a transformation function that is applied to a non-null value of a property
 * path evaluation.
 *
 * @param <X> The result type of the property path.
 * @param <I> The root type of the property path.
 * @param <E> The exception that may be thrown by the property path.
 */
public interface ApplyBuilder<X, I, E extends Exception> {

  /**
   * Specifies a function that is applied to the evaluation result of the property path. <b>The function will only be
   * applied in case the property path evaluates to a non-null value.</b>
   * 
   * <p>
   * If the function returns <code>null</code> the property path will not have a value.
   * </p>
   *
   * @param transformation The function to transform the property path result value.
   * @return Returns the {@link Get} instance.
   */
  public <O> GetAndApply<I, X, O, E> apply(Function<X, O> transformation);

}
