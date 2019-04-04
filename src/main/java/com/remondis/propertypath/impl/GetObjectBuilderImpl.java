package com.remondis.propertypath.impl;

import java.util.Optional;

import com.remondis.propertypath.api.GetObjectBuilder;
import com.remondis.propertypath.api.PropertyPath;
import com.remondis.propertypath.api.PropertyPathWithException;

public final class GetObjectBuilderImpl<I> implements GetObjectBuilder<I> {

  private Class<I> startType;
  private I object;

  public GetObjectBuilderImpl(Class<I> startType, I object) {
    this.startType = startType;
    this.object = object;
  }

  @Override
  public <O> Optional<O> evaluate(PropertyPath<I, O> selector) {
    return new GetImpl<I, O, RuntimeException>(startType, selector::selectProperty).from(object);
  }

  @Override
  public <O, E extends Exception> Optional<O> evaluateWithException(PropertyPathWithException<O, I, E> selector)
      throws E {
    return new GetImpl<I, O, E>(startType, selector).from(object);
  }

}
