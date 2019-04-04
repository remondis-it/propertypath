package com.remondis.propertypath.api;

import com.remondis.propertypath.impl.GetBuilderImpl;
import com.remondis.propertypath.impl.GetObjectBuilderImpl;

public final class Getter {

  public static <I, O> GetBuilder<I> newFor(Class<I> startType) {
    return new GetBuilderImpl<I>(startType);
  }

  @SuppressWarnings("unchecked")
  public static <I, O> GetObjectBuilder<I> from(I object) {
    return new GetObjectBuilderImpl<I>((Class<I>) object.getClass(), object);
  }

}
