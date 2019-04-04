package com.remondis.propertypath.impl;

import com.remondis.propertypath.api.Get;
import com.remondis.propertypath.api.GetBuilder;
import com.remondis.propertypath.api.PropertyPathWithException;

public class GetBuilderImpl<I> implements GetBuilder<I> {

  private Class<I> startType;

  public GetBuilderImpl(Class<I> startType) {
    this.startType = startType;
  }

  @Override
  public <O, E extends Exception> Get<I, O, E> evaluateWithException(PropertyPathWithException<O, I, E> selector)
      throws E {
    return new GetImpl<I, O, E>(startType, selector);
  }
}
