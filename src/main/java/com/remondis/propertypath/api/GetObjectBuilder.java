package com.remondis.propertypath.api;

import java.util.Optional;

public interface GetObjectBuilder<I> {

  public <O, E extends Exception> Optional<O> evaluate(PropertyPathWithException<O, I, E> selector) throws E;

}
