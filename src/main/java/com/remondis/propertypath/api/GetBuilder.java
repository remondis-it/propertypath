package com.remondis.propertypath.api;

public interface GetBuilder<I> {

  public default <O> Get<I, O, RuntimeException> evaluate(PropertyPath<I, O> selector) {
    return evaluateWithException(selector::selectProperty);
  }

  public <O, E extends Exception> Get<I, O, E> evaluateWithException(PropertyPathWithException<O, I, E> selector)
      throws E;
}
