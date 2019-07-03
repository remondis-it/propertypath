package com.remondis.propertypath.api;

import com.remondis.propertypath.impl.AssertGetterBuilderImpl;

/**
 * This is the testing API of the Property Path library. The API can be used to assert a specific property path. If the
 * expected property path and all containing arguments equal the actual property path and arguments of a given
 * {@link Get} instance, the test assert will succeed, otherwise an {@link AssertionError} is thrown with a message
 * explaining the differences.
 */
public class AssertGetter {

  /**
   * Creates a new builder to assert the property path of a specified {@link Get} instance.
   *
   * @param getter The {@link Get} instance under test.
   * @return Returns a builder for further configuration.
   */
  public static <I, O, E extends Exception> AssertGetterBuilder<I, O, E> of(Get<I, O, E> getter) {
    return new AssertGetterBuilderImpl<I, O, O, E>(getter);
  }

  /**
   * Creates a new builder to assert the property path of a specified {@link Get} instance.
   *
   * @param getter The {@link Get} instance under test.
   * @return Returns a builder for further configuration.
   */
  public static <I, O, T, E extends Exception> AssertGetterAndApplyBuilder<I, O, T, E> of(
      GetAndApply<I, O, T, E> getter) {
    return new AssertGetterBuilderImpl<I, O, T, E>(getter);
  }
}
