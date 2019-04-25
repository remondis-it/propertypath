package com.remondis.propertypath.impl;

import static java.util.Objects.requireNonNull;

import java.util.function.Function;

import com.remondis.propertypath.api.ApplyBuilder;
import com.remondis.propertypath.api.GetAndApply;
import com.remondis.propertypath.api.PropertyPath;

public class ApplyBuilderImpl<X, I, E extends Exception> implements ApplyBuilder<X, I, E> {

  private Class<I> startType;
  private PropertyPath<X, I, E> selector;

  public ApplyBuilderImpl(Class<I> startType, PropertyPath<X, I, E> selector) {
    requireNonNull(startType, "Type must not be null.");
    requireNonNull(selector, "Property path must not be null.");
    this.startType = startType;
    this.selector = selector;
  }

  @Override
  public <O> GetAndApply<I, X, O, E> apply(Function<X, O> transformation) {
    requireNonNull(transformation, "Fransformation function must not be null.");
    return new GetImpl<I, X, O, E>(startType, selector, transformation);
  }

}
