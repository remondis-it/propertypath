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
}
