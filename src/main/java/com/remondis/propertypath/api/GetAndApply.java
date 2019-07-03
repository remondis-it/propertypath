package com.remondis.propertypath.api;

public interface GetAndApply<I, O, T, E extends Exception> extends Get<I, T, E> {

  public GetWithoutException<I, T> toJdk8();

}