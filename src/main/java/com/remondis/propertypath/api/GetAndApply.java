package com.remondis.propertypath.api;

/**
 * A get access object that applies a mapping function on the result of a getter.
 *
 * @param <I> The input object type
 * @param <O> The property type.
 * @param <T> The type after applying the mapping function.
 * @param <E> The exception type that maybe thrown by the property path.
 */
public interface GetAndApply<I, O, T, E extends Exception> extends Get<I, T, E> {

  /**
   * @return Returns a get access object without the support for generic exceptions. This was introduced to provide a
   *         workaround for JDK 8, where a bug led to incorrect exception type inference.
   */
  public GetWithoutException<I, T> toJdk8();

}