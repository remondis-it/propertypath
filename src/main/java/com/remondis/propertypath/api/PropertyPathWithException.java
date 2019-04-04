package com.remondis.propertypath.api;

@FunctionalInterface
public interface PropertyPathWithException<O, I, E extends Exception> {
  O selectProperty(I i) throws E;

}
