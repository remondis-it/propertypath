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
 * Once the builder creates a {@link GetAndApply} instance, <b>this instance can and should be reused</b>. This library
 * creates
 * proxy classes which is a time consuming operation that is only neccessary once. <b>So cache the {@link GetAndApply}
 * instance
 * as long as possible.</b>
 * </p>
 *
 * <h2>Implementation hints</h2>
 * The implementation is expected to provide a valid {@link Object#equals(Object)} method. Two getters are considered
 * equal if their types, property paths and the respective argument values are equal.
 *
 * @param <I> The type to evaluate a property path on.
 * @param <X> The type of the property path evaluation before applying a transform function. Optional!
 * @param <O> The type of the property value.
 * @param <E> The exception type that may be thrown by the property path.
 */
public interface GetAndApply<I, X, O, E extends Exception> extends Get<I, O, E> {

}
