package com.remondis.propertypath.impl;

import java.util.Optional;

import com.remondis.propertypath.api.Get;
import com.remondis.propertypath.api.GetWithoutException;

public class GetWithoutExceptionImpl<I, O> implements GetWithoutException<I, O> {

  private Get<I, O, ?> getter;

  GetWithoutExceptionImpl(Get<I, O, ?> getter) {
    this.getter = getter;
  }

  @Override
  public Optional<O> from(I object) {
    try {
      return getter.from(object);
    } catch (Exception e) {
      throw new RuntimeException("Getter threw an exception.", e);
    }
  }

  @Override
  public O fromOrDefault(I object, O defaultValue) {
    try {
      return getter.fromOrDefault(object, defaultValue);
    } catch (Exception e) {
      throw new RuntimeException("Getter threw an exception.", e);
    }
  }

  @Override
  public String toString(boolean detailed) {
    return getter.toString(detailed);
  }

  @Override
  public String toString() {
    return getter.toString();
  }

}
