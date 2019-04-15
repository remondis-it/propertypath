package com.remondis.propertypath.api;

public interface GetBuilder<I> {

  public <O, E extends Exception> Get<I, O, E> evaluate(PropertyPathWithException<O, I, E> selector) throws E;
}
