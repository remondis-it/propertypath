package com.remondis.propertypath.features.getAndApply;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Test;

import com.remondis.propertypath.api.AssertGetter;
import com.remondis.propertypath.api.GetAndApply;
import com.remondis.propertypath.api.Getter;
import com.remondis.propertypath.common.Address;
import com.remondis.propertypath.common.Gender;
import com.remondis.propertypath.common.Person;

public class GetAndApplyTest {

  @Test
  public void shouldReturnNoValueIfFunctionReturnsNull() {
    Person person = new Person("forename", "name", 30, Gender.W,
        new Address("street", "houseNumber", "zipCode", "city"));

    Optional<String> opt = Getter.newFor(Person.class)
        .evaluateAnd(p -> p.getAddress()
            .getStreet())
        .apply((something) -> (String) null)
        .from(person);
    assertFalse(opt.isPresent());
  }

  @Test
  public void shouldReturnValueIfFunctionReturnsStaticValue() {
    Person person = new Person("forename", "name", 30, Gender.W,
        new Address("street", "houseNumber", "zipCode", "city"));

    String expectedStreet = "expectedStreet";

    Optional<String> opt = Getter.newFor(Person.class)
        .evaluateAnd(p -> p.getAddress()
            .getStreet())
        .apply((something) -> expectedStreet)
        .from(person);
    assertTrue(opt.isPresent());
    assertEquals(expectedStreet, opt.get());
  }

  @Test
  public void shouldAssertHappyPath() {
    GetAndApply<Person, String, Integer, RuntimeException> getAndApply = Getter.newFor(Person.class)
        .evaluateAnd(p -> p.getAddress()
            .getStreet())
        .apply(String::length);

    AssertGetter.of(getAndApply)
        .assertGetterWithTransform(p -> p.getAddress()
            .getStreet());
  }

  @Test
  public void shouldFailOnNotMatchingPropertyPath() {
    GetAndApply<Person, String, Integer, RuntimeException> getAndApply = Getter.newFor(Person.class)
        .evaluateAnd(p -> p.getAddress()
            .getStreet())
        .apply(String::length);

    assertThatThrownBy(() -> AssertGetter.of(getAndApply)
        .assertGetterWithTransform(p -> p.getAddress()
            .getHouseNumber())).isInstanceOf(AssertionError.class);
  }

  @Test
  public void shouldGetAndApply() {
    String expectedStreet = "street";
    Person person = new Person("forename", "name", 30, Gender.W,
        new Address(expectedStreet, "houseNumber", "zipCode", "city"));

    GetAndApply<Person, String, Integer, RuntimeException> getAndApply = Getter.newFor(Person.class)
        .evaluateAnd(p -> p.getAddress()
            .getStreet())
        .apply(String::length);

    Optional<Integer> optStringLength = getAndApply.from(person);

    assertTrue(optStringLength.isPresent());
    assertEquals(expectedStreet.length(), (int) optStringLength.get());
  }

  @Test
  public void shouldNotApplyFunctionIfNull_1() {
    Person person = new Person("forename", "name", 30, Gender.W, new Address(null, "houseNumber", "zipCode", "city"));

    Optional<Integer> optStringLength = Getter.newFor(Person.class)
        .evaluateAnd(p -> p.getAddress()
            .getStreet())
        .apply(String::length)
        .from(person);

    assertFalse(optStringLength.isPresent());
  }

  @Test
  public void shouldNotApplyFunctionIfNull_2() {
    Person person = new Person("forename", "name", 30, Gender.W, null);

    Optional<Integer> optStringLength = Getter.newFor(Person.class)
        .evaluateAnd(p -> p.getAddress()
            .getStreet())
        .apply(String::length)
        .from(person);

    assertFalse(optStringLength.isPresent());
  }
}
