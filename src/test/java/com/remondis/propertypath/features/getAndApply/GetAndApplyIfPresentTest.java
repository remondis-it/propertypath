package com.remondis.propertypath.features.getAndApply;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Test;

import com.remondis.propertypath.api.AssertGetter;
import com.remondis.propertypath.api.Get;
import com.remondis.propertypath.api.GetAndApply;
import com.remondis.propertypath.api.Getter;
import com.remondis.propertypath.common.Address;
import com.remondis.propertypath.common.Gender;
import com.remondis.propertypath.common.Person;

public class GetAndApplyIfPresentTest {

  @Test
  public void shouldFailOnNotMatchingPropertyPath() {
    GetAndApply<Person, String, Integer, RuntimeException> getAndApply = Getter.newFor(Person.class)
        .evaluate(p -> p.getAddress()
            .getStreet())
        .andApplyIfPresent(String::length);

    assertThatThrownBy(() -> AssertGetter.of(getAndApply)
        .assertGetter(p -> p.getAddress()
            .getHouseNumber())).isInstanceOf(AssertionError.class)
                .hasMessageContaining(
                    "The expected propertypath does not match the actual property path of this getter:");
  }

  @Test
  public void shouldFailOnNotMatchingIfPresentStrategy() {
    GetAndApply<Person, String, Integer, RuntimeException> getAndApply = Getter.newFor(Person.class)
        .evaluate(p -> p.getAddress()
            .getStreet())
        .andApplyIfPresent(String::length);

    assertThatThrownBy(() -> AssertGetter.of(getAndApply)
        .assertGetter(p -> p.getAddress()
            .getStreet())
        .andApplyAlways(s -> 1)).isInstanceOf(AssertionError.class)
            .hasMessage(
                "The getter was expected to always apply the mapping function, but actually the getter is configured to only apply the mapping function if a value is present.");
  }

  @Test
  public void shouldReturnNoValueIfFunctionReturnsNull() {
    Person person = new Person("forename", "name", 30, Gender.W,
        new Address("street", "houseNumber", "zipCode", "city"));

    Optional<String> opt = Getter.newFor(Person.class)
        .evaluate(p -> p.getAddress()
            .getStreet())
        .andApplyIfPresent((something) -> (String) null)
        .from(person);
    assertFalse(opt.isPresent());
  }

  @Test
  public void shouldReturnValueIfFunctionReturnsStaticValue() {
    Person person = new Person("forename", "name", 30, Gender.W,
        new Address("street", "houseNumber", "zipCode", "city"));

    String expectedStreet = "expectedStreet";

    Optional<String> opt = Getter.newFor(Person.class)
        .evaluate(p -> p.getAddress()
            .getStreet())
        .andApplyIfPresent((something) -> expectedStreet)
        .from(person);
    assertTrue(opt.isPresent());
    assertEquals(expectedStreet, opt.get());
  }

  @Test
  public void shouldAssertHappyPath() {
    GetAndApply<Person, String, Integer, RuntimeException> getter = Getter.newFor(Person.class)
        .evaluate(p -> p.getAddress()
            .getStreet())
        .andApplyIfPresent(String::length);

    AssertGetter.of(getter)
        .assertGetter(p -> p.getAddress()
            .getStreet())
        .andApplyIfPresent();
  }

  @Test
  public void shouldGetAndApply() {
    String expectedStreet = "street";
    Person person = new Person("forename", "name", 30, Gender.W,
        new Address(expectedStreet, "houseNumber", "zipCode", "city"));

    Get<Person, Integer, RuntimeException> getAndApply = Getter.newFor(Person.class)
        .evaluate(p -> p.getAddress()
            .getStreet())
        .andApplyIfPresent(String::length);

    Optional<Integer> optStringLength = getAndApply.from(person);

    assertTrue(optStringLength.isPresent());
    assertEquals(expectedStreet.length(), (int) optStringLength.get());
  }

  @Test
  public void shouldNotApplyFunctionIfNull_1() {
    Person person = new Person("forename", "name", 30, Gender.W, new Address(null, "houseNumber", "zipCode", "city"));

    Optional<Integer> optStringLength = Getter.newFor(Person.class)
        .evaluate(p -> p.getAddress()
            .getStreet())
        .andApplyIfPresent(String::length)
        .from(person);

    assertFalse(optStringLength.isPresent());
  }

  @Test
  public void shouldNotApplyFunctionIfNull_2() {
    Person person = new Person("forename", "name", 30, Gender.W, null);

    Optional<Integer> optStringLength = Getter.newFor(Person.class)
        .evaluate(p -> p.getAddress()
            .getStreet())
        .andApplyIfPresent(String::length)
        .from(person);

    assertFalse(optStringLength.isPresent());
  }
}
