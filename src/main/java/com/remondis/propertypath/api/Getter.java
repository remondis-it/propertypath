package com.remondis.propertypath.api;

import static java.util.Objects.requireNonNull;

import com.remondis.propertypath.impl.GetBuilderImpl;

/**
 * This is the Property Path API that can be used to build null-safe getters evaluating a specific property
 * path.
 * <p>
 * This library can be used to build get-chains easier. If you have a lot of optional fields in a large Java Bean object
 * graph you normally have to implement null checks whenever accessing an optional field. This library performs this
 * null checks so a property path can be written like <code>a.getB().getC()</code>. The return value of
 * <tt>getB()</tt> does not have to be checked for <code>null</code>.
 * </p>
 *
 * <p>
 * A property path is a chain of get-calls. If a getter returns <code>null</code> the property path does not have a
 * value. The same is true for lists and maps that are either <code>null</code> or do not contain a specified index or
 * key. The null-checks are performed by the library an must not be implemented in the property path.
 * </p>
 *
 * This is the entry point of the builder API.
 *
 * <h2>Important information</h2>
 * <p>
 * Once the builder creates a {@link Get} instance, <b>this instance can and should be reused</b>. This library creates
 * proxy classes which is a time consuming operation that is only neccessary once. <b>So cache the {@link Get} instance
 * as long as possible.</b>
 * </p>
 */
public final class Getter {

  /**
   * Creates a builder of a property path for the specified type.
   *
   * @param startType The type to evaluate a property path on.
   * @return Returns a builder for further configuration.
   */
  public static <I> GetBuilder<I> newFor(Class<I> startType) {
    requireNonNull(startType, "Type must not be null.");
    return new GetBuilderImpl<>(startType);
  }

}
