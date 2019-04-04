package com.remondis.propertypath.api;

import java.util.Optional;

public interface GetObjectBuilder<I> {

  public default <O> Optional<O> evaluate(PropertyPath<I, O> selector) {
    return evaluateWithException(selector::selectProperty);
  }

  public <O, E extends Exception> Optional<O> evaluateWithException(PropertyPathWithException<O, I, E> selector)
      throws E;

}
