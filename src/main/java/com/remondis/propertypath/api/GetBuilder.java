package com.remondis.propertypath.api;

import java.util.List;
import java.util.Map;

/**
 * This is the builder stage for specifying a property path.
 *
 * @param <I>
 */
public interface GetBuilder<I> {

  /**
   * Specifies the property path to evaluate. The property path is only allowed to perform getter calls on the provided
   * proxy object. The methods {@link List#get(int)} and {@link Map#get(Object)} are also supported.
   *
   * <h2>Exceptions</h2>
   * If the property path throws an exception, the generic exception type comes in place. When the property path is
   * evaluated, it may throw exceptions of this type later.
   *
   * <h2>Don't</h2>
   * The specified property path is applied to a proxy object to determine the actual calls. The property path is not a
   * function that is really invoked on the real object later. For this reason it is not possible to manipulate or
   * calculate the data
   * in the propety path. <b>The property path is not a function operating on real objects!</b>
   *
   * @param selector A lambda function performing get calls on the specified object to declare the actual property path.
   *        <b>This is not a function operating on real object. So do not manipulate or calculate here!</b>
   * @return Returns the final {@link Get} instance.
   */
  public <O, E extends Exception> Get<I, O, E> evaluate(PropertyPath<O, I, E> selector) throws E;
}
