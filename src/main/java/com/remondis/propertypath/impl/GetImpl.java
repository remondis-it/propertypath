package com.remondis.propertypath.impl;

import java.util.Optional;

import com.remondis.propertypath.api.Get;
import com.remondis.propertypath.api.PropertyPathWithException;
import com.remondis.propertypath.impl.exceptions.ExceptionInPropertyPath;
import com.remondis.propertypath.impl.exceptions.NotAValidPropertyPathException;
import com.remondis.propertypath.impl.exceptions.ZeroInteractionException;

public final class GetImpl<I, O, E extends Exception> implements Get<I, O, E> {

  private Class<I> startType;
  private PropertyPathWithException<O, I, E> selector;

  public GetImpl(Class<I> startType, PropertyPathWithException<O, I, E> selector) {
    this.startType = startType;
    this.selector = selector;
  }

  @Override
  public Optional<O> from(I object) throws E {
    try {
      TypedTransitiveProperty<I, O, E> sourceProperty = InvocationSensor.getTransitiveTypedProperty(startType,
          selector);
      O returnValue = sourceProperty.get(object);
      return Optional.ofNullable(returnValue);
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

}
