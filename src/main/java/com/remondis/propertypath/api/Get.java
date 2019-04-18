package com.remondis.propertypath.api;

import java.util.Optional;

/**
 * This class represents a getter that can be evaluated on a real object instance of a matching type. The getter returns
 * instances of {@link Optional}. If the value of the evaluated property path is present, it can be accessed through
 * {@link Optional#get()}. If the property path does not have a value, an empty {@link Optional} is returned.
 * <p>
 * A property path is a chain of get-calls. If a getter returns <code>null</code> the property path does not have a
 * value. The same is true for lists and maps that are either <code>null</code> or do not contain a specified index or
 * key. The null-checks are performed by the library an must not be implemented in the property path.
 * </p>
 *
 * <h2>Important information</h2>
 * <p>
 * Once the builder creates a {@link Get} instance, <b>this instance can and should be reused</b>. This library creates
 * proxy classes which is a time consuming operation that is only neccessary once. <b>So cache the {@link Get} instance
 * as long as possible.</b>
 * </p>
 *
 * <h2>Implementation hints</h2>
 * The implementation is expected to provide a valid {@link Object#equals(Object)} method. Two getters are considered
 * equal if their types, property paths and the respective argument values are equal.
 *
 * @param <I> The type to evaluate a property path on.
 * @param <O> The type of the property value.
 * @param <E> The exception type that may be thrown by the property path.
 */
public interface Get<I, O, E extends Exception> {

  /**
   * Evaluates the property path on the specified object.
   *
   * @param object The object to evaluate the property path on.
   * @return Returns the value of the property wrapped in an {@link Optional}.
   * @throws E May be thrown by the property path.
   */
  public Optional<O> from(I object) throws E;

  /**
   * Evaluates the property path on the specified object. If a value is present it will be returned, otherwise the
   * specified default value is returned.
   *
   * @param object The object to evaluate the property path on.
   * @param defaultValue The default value to return in case the property path does not have a value.
   * @return If a value is present it will be returned, otherwise the
   *         specified default value is returned.
   * @throws E May be thrown by the property path.
   */
  public O fromOrDefault(I object, O defaultValue) throws E;

  /**
   * @return Returns a detailed string if specified, otherwise a shorter string is returned.
   */
  public String toString(boolean detailed);

}
