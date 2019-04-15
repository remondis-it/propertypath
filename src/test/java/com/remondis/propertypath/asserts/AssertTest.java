package com.remondis.propertypath.asserts;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.Test;

import com.remondis.propertypath.api.AssertGetter;
import com.remondis.propertypath.api.Get;
import com.remondis.propertypath.api.Getter;
import com.remondis.propertypath.common.Person;

public class AssertTest {

  @Test
  public void shouldSucceed() {
    Get<Person, String, RuntimeException> getter = Getter.newFor(Person.class)
        .evaluate(p -> p.getAddress()
            .getStreet());

    AssertGetter.of(getter)
        .assertGetter(p -> p.getAddress()
            .getStreet());
  }

  @Test
  public void shouldDetectDifferentPropertyPath() {
    Get<Person, String, RuntimeException> getter = Getter.newFor(Person.class)
        .evaluate(p -> p.getAddress()
            .getStreet());

    assertThatThrownBy(() -> AssertGetter.of(getter)
        .assertGetter(p -> p.getAddress()
            .getHouseNumber())).isInstanceOf(AssertionError.class);
  }

}
