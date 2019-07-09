package com.remondis.propertypath.api;

import static java.util.Objects.isNull;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
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

  /**
   * @param <S> The collection's element type.
   * @param <C> The collection type
   * @return Returns a mapping function, that maps to {@link Collections#emptyList()} if the input is <code>null</code>.
   */
  public static <S, C extends List<? super S>> Function<C, List<? super S>> emptyListIfNull() {
    return collection -> isNull(collection) ? Collections.emptyList() : collection;
  }

  /**
   * @param <S> The collection's element type.
   * @param <C> The collection type
   * @return Returns a mapping function, that maps to {@link Collections#emptyMap()} if the input is <code>null</code>.
   */
  public static <S, T, C extends Map<? super S, ? super T>> Function<C, Map<? super S, ? super T>> emptyMapIfNull() {
    return collection -> isNull(collection) ? Collections.emptyMap() : collection;
  }

  /**
   * @param <S> The collection's element type.
   * @param <C> The collection type
   * @return Returns a mapping function, that maps to {@link Collections#emptyEnumeration()} if the input is
   *         <code>null</code>.
   */
  public static <S, C extends Enumeration<? super S>> Function<C, Enumeration<? super S>> emptyEnumerationIfNull() {
    return collection -> isNull(collection) ? Collections.emptyEnumeration() : collection;
  }

  /**
   * @param <S> The collection's element type.
   * @param <C> The collection type
   * @return Returns a mapping function, that maps to {@link Collections#emptyIterator()} if the input is
   *         <code>null</code>.
   */
  public static <S, C extends Iterator<? super S>> Function<C, Iterator<? super S>> emptyIteratorIfNull() {
    return collection -> isNull(collection) ? Collections.emptyIterator() : collection;
  }

  /**
   * @param <S> The collection's element type.
   * @param <C> The collection type
   * @return Returns a mapping function, that maps to {@link Collections#emptyListIterator()} if the input is
   *         <code>null</code>.
   */
  public static <S, C extends ListIterator<? super S>> Function<C, Iterator<? super S>> emptyListIteratorIfNull() {
    return collection -> isNull(collection) ? Collections.emptyListIterator() : collection;
  }
}
