package com.remondis.propertypath.impl;

import static java.util.Objects.isNull;

import java.util.Optional;
import java.util.function.Function;

import com.remondis.propertypath.api.Get;
import com.remondis.propertypath.api.GetAndApply;
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
  public <X> GetAndApply<I, O, X, E> andApply(Function<O, X> mapping) {
    return new GetAndApplyImpl<I, O, X, E>(this, mapping);
  }

  @Override
  public Optional<O> from(I object) throws E {
    if (isNull(object)) {
      return Optional.empty();
    }
    O evaluationValue = sourceProperty.get(object);
    if (isNull(evaluationValue)) {
      return Optional.empty();
    } else {
      return Optional.ofNullable(evaluationValue);
    }
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

  protected static <I, X, E extends Exception> TypedTransitiveProperty<I, X, E> buildTransitiveProperty(
      Class<I> startType, PropertyPath<X, I, E> selector) {
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

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((sourceProperty == null) ? 0 : sourceProperty.hashCode());
    result = prime * result + ((startType == null) ? 0 : startType.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    GetImpl other = (GetImpl) obj;
    if (sourceProperty == null) {
      if (other.sourceProperty != null)
        return false;
    } else if (!sourceProperty.equals(other.sourceProperty))
      return false;
    if (startType == null) {
      if (other.startType != null)
        return false;
    } else if (!startType.equals(other.startType))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return sourceProperty.toString(false);
  }

  /**
   * @return Returns a detailed string if specified, otherwise a shorter string is returned.
   */
  @Override
  public String toString(boolean detailed) {
    return sourceProperty.toString(detailed);
  }

  @Override
  public String toPath() {
    return sourceProperty.toPath();
  }

}
