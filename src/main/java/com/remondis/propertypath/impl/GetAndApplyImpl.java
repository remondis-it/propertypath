package com.remondis.propertypath.impl;

import static java.util.Objects.requireNonNull;

import java.util.Optional;
import java.util.function.Function;

import com.remondis.propertypath.api.Get;
import com.remondis.propertypath.api.GetAndApply;
import com.remondis.propertypath.api.GetWithoutException;

public class GetAndApplyImpl<I, O, T, E extends Exception> implements GetAndApply<I, O, T, E> {

  private Get<I, O, E> getter;
  private Function<O, T> mapping;
  private boolean applyIfPresent;

  public GetAndApplyImpl(Get<I, O, E> getter, Function<O, T> mapping, boolean applyIfPresent) {
    this.getter = getter;
    this.mapping = mapping;
    this.applyIfPresent = applyIfPresent;
  }

  @Override
  public GetWithoutException<I, T> toJdk8() {
    return new GetWithoutExceptionImpl<I, T>(this);
  }

  boolean isApplyIfPresent() {
    return applyIfPresent;
  }

  @SuppressWarnings("unchecked")
  Get<I, O, E> getGetter() {
    if (getter instanceof GetImpl) {
      return getter;
    } else if (getter instanceof GetAndApplyImpl) {
      GetAndApplyImpl getAndApplyImpl = (GetAndApplyImpl) getter;
      return getAndApplyImpl.getGetter();
    } else {
      throw new IllegalStateException("Cannot determine original getter. This is an implementation fault.");
    }
  }

  Function<O, T> getMapping() {
    return mapping;
  }

  @Override
  public Optional<T> from(I object) throws E {
    Optional<O> opt = getter.from(object);
    if (opt.isPresent()) {
      T mappedValue = mapping.apply(opt.get());
      return Optional.ofNullable(mappedValue);
    } else {
      if (applyIfPresent) {
        return Optional.empty();
      } else {
        T mappedValue = mapping.apply(null);
        return Optional.ofNullable(mappedValue);
      }
    }
  }

  @Override
  public T fromOrDefault(I object, T defaultValue) throws E {
    Optional<T> opt = from(object);
    if (opt.isPresent()) {
      return opt.get();
    } else {
      return defaultValue;
    }
  }

  @Override
  public <X> GetAndApply<I, T, X, E> andApplyIfPresent(Function<T, X> mapping) {
    requireNonNull(mapping, "The mapping function must not be null.");
    return new GetAndApplyImpl<I, T, X, E>(this, mapping, true);
  }

  @Override
  public <X> GetAndApply<I, T, X, E> andApply(Function<T, X> mapping) {
    requireNonNull(mapping, "The mapping function must not be null.");
    return new GetAndApplyImpl<I, T, X, E>(this, mapping, false);
  }

  @Override
  public String toString(boolean detailed) {
    return getter.toString(detailed) + " and applying a mapping function!";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (applyIfPresent ? 1231 : 1237);
    result = prime * result + ((getter == null) ? 0 : getter.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    GetAndApplyImpl other = (GetAndApplyImpl) obj;
    if (applyIfPresent != other.applyIfPresent)
      return false;
    if (getter == null) {
      if (other.getter != null)
        return false;
    } else if (!getter.equals(other.getter))
      return false;
    return true;
  }

}
