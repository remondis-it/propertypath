package com.remondis.propertypath.impl;

import static java.util.Objects.requireNonNull;

import com.remondis.propertypath.api.Get;
import com.remondis.propertypath.api.GetBuilder;
import com.remondis.propertypath.api.PropertyPath;

public class GetBuilderImpl<I> implements GetBuilder<I> {

  private Class<I> startType;

  public GetBuilderImpl(Class<I> startType) {
    this.startType = startType;
  }

  @Override
  public <O, E extends Exception> Get<I, O, E> evaluate(PropertyPath<O, I, E> selector) {
    requireNonNull(selector, "Property path must not be null.");
    return new GetImpl<>(startType, selector);
  }

}
