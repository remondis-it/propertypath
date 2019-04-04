package com.remondis.propertypath.api;

@FunctionalInterface
public interface PropertyPath<T, R> {
  R selectProperty(T t);
}
