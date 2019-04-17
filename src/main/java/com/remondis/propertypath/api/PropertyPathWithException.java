package com.remondis.propertypath.api;

import java.util.List;
import java.util.Map;

/**
 * This functional interface defines a property path. It's basically a function that is applied to a proxy object at
 * runtime. The property path function is expected to invoke a get-method chain specifying a valid traversal through
 * Java Bean properties (with a support for {@link List} and {@link Map}).
 *
 * @param <I> The type to evaluate a property path on.
 * @param <O> The type of the property value to return.
 * @param <E> The exception type that may be thrown by the get invocation chain.
 */
@FunctionalInterface
public interface PropertyPathWithException<O, I, E extends Exception> {
  /**
   * Performs a get-method invocation chain on the specified input object to declare a property path. Only get-method
   * calls and calls to {@link List#get(int)} and {@link Map#get(Object)} are allowed.
   * <p>
   * <b>Please read the explanation of
   * the property path in {@link GetBuilder#evaluate(PropertyPathWithException)}!</b>
   * </p>
   *
   * @param t A proxy object to perform get-calls on. <b>Do not manipulate or calculate here! This is not a function
   *        operating on real objects!</b>
   */
  O selectProperty(I i) throws E;

}
