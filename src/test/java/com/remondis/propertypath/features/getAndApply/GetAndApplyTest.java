package com.remondis.propertypath.features.getAndApply;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import com.remondis.propertypath.api.AssertGetter;
import com.remondis.propertypath.api.Get;
import com.remondis.propertypath.api.GetAndApply;
import com.remondis.propertypath.api.Getter;
import com.remondis.propertypath.common.Address;
import com.remondis.propertypath.common.Gender;
import com.remondis.propertypath.common.Person;
import org.junit.jupiter.api.Test;

class GetAndApplyTest {

  @Test
  void shouldReturnNoValueIfFunctionReturnsNull() {
    Person person = new Person("forename", "name", 30, Gender.W,
        new Address("street", "houseNumber", "zipCode", "city"));

    Optional<String> opt = Getter.newFor(Person.class)
        .evaluate(p -> p.getAddress()
            .getStreet())
        .andApply((something) -> (String) null)
        .from(person);
    assertFalse(opt.isPresent());
  }

  @Test
  void shouldReturnValueIfFunctionReturnsStaticValue() {
    Person person = new Person("forename", "name", 30, Gender.W,
        new Address("street", "houseNumber", "zipCode", "city"));

    String expectedStreet = "expectedStreet";

    Optional<String> opt = Getter.newFor(Person.class)
        .evaluate(p -> p.getAddress()
            .getStreet())
        .andApply((something) -> expectedStreet)
        .from(person);
    assertTrue(opt.isPresent());
    assertEquals(expectedStreet, opt.get());
  }

  @Test
  void shouldAssertHappyPath() {
    GetAndApply<Person, String, Integer, RuntimeException> getter = Getter.newFor(Person.class)
        .evaluate(p -> p.getAddress()
            .getStreet())
        .andApply(String::length);

    AssertGetter.of(getter)
        .assertGetter(p -> p.getAddress()
            .getStreet());
  }

  @Test
  void shouldFailOnNotMatchingPropertyPath() {
    GetAndApply<Person, String, Integer, RuntimeException> getAndApply = Getter.newFor(Person.class)
        .evaluate(p -> p.getAddress()
            .getStreet())
        .andApply(String::length);

    assertThatThrownBy(() -> AssertGetter.of(getAndApply)
        .assertGetter(p -> p.getAddress()
            .getHouseNumber()))
        .isInstanceOf(AssertionError.class);
  }

  @Test
  void shouldGetAndApply() {
    String expectedStreet = "street";
    Person person = new Person("forename", "name", 30, Gender.W,
        new Address(expectedStreet, "houseNumber", "zipCode", "city"));

    Get<Person, Integer, RuntimeException> getAndApply = Getter.newFor(Person.class)
        .evaluate(p -> p.getAddress()
            .getStreet())
        .andApply(String::length);

    Optional<Integer> optStringLength = getAndApply.from(person);

    assertTrue(optStringLength.isPresent());
    assertEquals(expectedStreet.length(), (int) optStringLength.get());
  }

  @Test
  void shouldNotApplyFunctionIfNull_1() {
    Person person = new Person("forename", "name", 30, Gender.W, new Address(null, "houseNumber", "zipCode", "city"));

    Optional<Integer> optStringLength = Getter.newFor(Person.class)
        .evaluate(p -> p.getAddress()
            .getStreet())
        .andApply(String::length)
        .from(person);

    assertFalse(optStringLength.isPresent());
  }

  @Test
  void shouldNotApplyFunctionIfNull_2() {
    Person person = new Person("forename", "name", 30, Gender.W, null);

    Optional<Integer> optStringLength = Getter.newFor(Person.class)
        .evaluate(p -> p.getAddress()
            .getStreet())
        .andApply(String::length)
        .from(person);

    assertFalse(optStringLength.isPresent());
  }
}
