package com.remondis.propertypath.api;

import static java.util.Objects.isNull;

import java.util.function.Function;

/**
 * This util class is a collection of handy mapping functions that can be used in conjunction with
 * {@link ApplyBuilder#apply(java.util.function.Function)}.
 */
public class MapTo {
  /**
   * @return Returns a {@link Function} that can be used to transform a {@link String} into <code>null</code> if the
   *         string is empty. This can be used in conjunction with {@link ApplyBuilder#apply(Function)}.
   */
  public static Function<String, String> nullIfEmpty() {
    return str -> isNull(str) || str.isEmpty() ? null : str;
  }

  /**
   * Returns <code>null</code> if the input value is <code>null</code>, otherwise the mapping function is applied to the
   * input.
   *
   * @param <S> The input type
   * @param <T> The output type
   * @param mapping The mapping function, applied if the input is non-null.
   * @return Returns a function that wraps the mapping function and only applies the mapping function if the input value
   *         is non-null.
   */
  public static <S, T> Function<S, T> nullOr(Function<S, T> mapping) {
    return s -> isNull(s) ? null : mapping.apply(s);
  }
}
