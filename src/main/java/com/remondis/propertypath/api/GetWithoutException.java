package com.remondis.propertypath.api;

import java.util.Optional;

/**
 * This is a {@link Get} interface but without exception support. The exception support can led to JDK 8 bugs when type
 * inference for exception is to perform. To provide a workaround for this, this interface exists.
 *
 * @param <I> The type to evaluate a property path on.
 * @param <O> The type of the property value.
 */
public interface GetWithoutException<I, O> {

  /**
   * Evaluates the property path on the specified object.
   *
   * @param object The object to evaluate the property path on.
   * @return Returns the value of the property wrapped in an {@link Optional}.
   * @throws E May be thrown by the property path.
   */
  public Optional<O> from(I object);

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
  public O fromOrDefault(I object, O defaultValue);

  /**
   * @return Returns a detailed string if specified, otherwise a shorter string is returned.
   */
  public String toString(boolean detailed);

}
