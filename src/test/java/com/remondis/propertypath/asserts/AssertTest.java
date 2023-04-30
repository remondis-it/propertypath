package com.remondis.propertypath.asserts;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.remondis.propertypath.api.AssertGetter;
import com.remondis.propertypath.api.Get;
import com.remondis.propertypath.api.Getter;
import com.remondis.propertypath.common.A;
import com.remondis.propertypath.common.DummyException;
import com.remondis.propertypath.common.Person;
import org.junit.jupiter.api.Test;

class AssertTest {

  @Test
  void shouldSucceed() {
    Get<Person, String, RuntimeException> getter = Getter.newFor(Person.class)
        .evaluate(p -> p.getAddress()
            .getStreet());

    AssertGetter.of(getter)
        .assertGetter(p -> p.getAddress()
            .getStreet());
  }

  @Test
  void shouldDetectDifferentPropertyPathDueToDifferentArguments() {
    assertThrows(AssertionError.class, () -> {
      Get<A, String, DummyException> get99 = Getter.newFor(A.class)
          .evaluate(al -> al.getB()
              .getCs()
              .get(99)
              .getString());
      AssertGetter.of(get99)
          .assertGetter(al -> al.getB()
              .getCs()
              .get(88)
              .getString());
    });
  }

  @Test
  void shouldDetectDifferentPropertyPath() {
    Get<Person, String, RuntimeException> getter = Getter.newFor(Person.class)
        .evaluate(p -> p.getAddress()
            .getStreet());

    assertThatThrownBy(() -> AssertGetter.of(getter)
        .assertGetter(p -> p.getAddress()
            .getHouseNumber()))
        .isInstanceOf(AssertionError.class);
  }

}
