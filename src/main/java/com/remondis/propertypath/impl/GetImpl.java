package com.remondis.propertypath.impl;

import static java.util.Objects.isNull;

import java.util.Optional;

import com.remondis.propertypath.api.Get;
import com.remondis.propertypath.api.PropertyPath;
import com.remondis.propertypath.impl.exceptions.ExceptionInPropertyPath;
import com.remondis.propertypath.impl.exceptions.NotAValidPropertyPathException;
import com.remondis.propertypath.impl.exceptions.ZeroInteractionException;

public final class GetImpl<I, O, E extends Exception> implements Get<I, O, E> {

  private Class<I> startType;
  private TypedTransitiveProperty<I, O, E> sourceProperty;

  public GetImpl(Class<I> startType, PropertyPath<O, I, E> selector) {
    this.startType = startType;
    this.sourceProperty = buildTransitiveProperty(startType, selector);
  }

  @Override
  public Optional<O> from(I object) throws E {
    if (isNull(object)) {
      return Optional.empty();
    }
    O returnValue = sourceProperty.get(object);
    return Optional.ofNullable(returnValue);
  }

  /**
   * @return Returns the {@link TypedTransitiveProperty} used to evaluate asserts.
   */
  protected TypedTransitiveProperty<I, O, E> getTransitiveProperty() {
    return sourceProperty;
  }

  protected Class<I> getStartType() {
    return startType;
  }

  protected static <I, O, E extends Exception> TypedTransitiveProperty<I, O, E> buildTransitiveProperty(
      Class<I> startType, PropertyPath<O, I, E> selector) {
    try {
      return InvocationSensor.getTransitiveTypedProperty(startType, selector);
    } catch (ZeroInteractionException e) {
      throw new PropertyPathException("The specified property path did not interact with the given object.", e);
    } catch (ExceptionInPropertyPath e) {
      throw new PropertyPathException("The specified property path threw an exception.", e);
    } catch (NotAValidPropertyPathException e) {
      throw new PropertyPathException(
          "The specified property path contained illegal method calls. Only getters and calls to List.get(int) and Map.get(Object) are allowed!",
          e);
    }
  }

  @Override
  public O fromOrDefault(I object, O defaultValue) throws E {
    Optional<O> optional = from(object);
    if (optional.isPresent()) {
      return optional.get();
    } else {
      return defaultValue;
    }
  }

}
