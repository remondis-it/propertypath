package com.remondis.propertypath.features.getAndApply;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Optional;
import java.util.function.Function;

import org.junit.Test;

import com.remondis.propertypath.api.AssertGetter;
import com.remondis.propertypath.api.GetAndApply;
import com.remondis.propertypath.api.Getter;
import com.remondis.propertypath.api.MapTo;
import com.remondis.propertypath.common.Address;
import com.remondis.propertypath.common.Gender;
import com.remondis.propertypath.common.Person;

public class GetAndApplyTest {

  @Test
  public void shouldFailOnNotMatchingPropertyPath() {
    GetAndApply<Person, String, Integer, RuntimeException> getAndApply = Getter.newFor(Person.class)
        .evaluate(p -> p.getAddress()
            .getStreet())
        .andApply(String::length);

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
        .andApply(String::length);

    assertThatThrownBy(() -> AssertGetter.of(getAndApply)
        .assertGetter(p -> p.getAddress()
            .getStreet())
        .andApplyIfPresent()).isInstanceOf(AssertionError.class)
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
        .andApply((something) -> (String) null)
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
        .andApply((something) -> expectedStreet)
        .from(person);
    assertTrue(opt.isPresent());
    assertEquals(expectedStreet, opt.get());
  }

  @Test
  public void shouldGetAndApply() {
    String expectedStreet = "street";
    Person person = new Person("forename", "name", 30, Gender.W,
        new Address(expectedStreet, "houseNumber", "zipCode", "city"));

    GetAndApply<Person, String, Integer, RuntimeException> getAndApply = Getter.newFor(Person.class)
        .evaluate(p -> p.getAddress()
            .getStreet())
        .andApply(lengthOrNull());

    AssertGetter.of(getAndApply)
        .assertGetter(p -> p.getAddress()
            .getStreet())
        .andApplyAlways(lengthOrNull());

    Optional<Integer> optStringLength = getAndApply.from(person);

    assertTrue(optStringLength.isPresent());
    assertEquals(expectedStreet.length(), (int) optStringLength.get());
  }

  private Function<String, Integer> lengthOrNull() {
    return MapTo.nullOr(String::length);
  }

  @Test
  public void shouldApplyFunctionEvenIfNull_1() {
    String expectedStreet = "expectedStreet";

    Person person = new Person("forename", "name", 30, Gender.W, new Address(null, "houseNumber", "zipCode", "city"));

    Optional<String> optStringLength = Getter.newFor(Person.class)
        .evaluate(p -> p.getAddress()
            .getStreet())
        .andApply(str -> expectedStreet)
        .from(person);

    assertTrue(optStringLength.isPresent());
  }

  @Test
  public void shouldApplyFunctionEvenIfNull_2() {
    String expectedStreet = "expectedStreet";

    Person person = new Person("forename", "name", 30, Gender.W, null);

    Optional<String> optStringLength = Getter.newFor(Person.class)
        .evaluate(p -> p.getAddress()
            .getStreet())
        .andApply(str -> expectedStreet)
        .from(person);

    assertTrue(optStringLength.isPresent());
  }
}
