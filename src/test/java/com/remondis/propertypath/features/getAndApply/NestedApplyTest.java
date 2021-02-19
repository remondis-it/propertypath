package com.remondis.propertypath.features.getAndApply;

import static java.util.Objects.isNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Optional;
import java.util.function.Function;

import org.junit.Test;

import com.remondis.propertypath.api.AssertGetter;
import com.remondis.propertypath.api.GetAndApply;
import com.remondis.propertypath.api.Getter;
import com.remondis.propertypath.common.Address;
import com.remondis.propertypath.common.Gender;
import com.remondis.propertypath.common.Person;

public class NestedApplyTest {

  @Test
  public void shouldReturnNestedValues() {
    GetAndApply<Person, String, String, RuntimeException> getter = Getter.newFor(Person.class)
        .evaluate(p -> p.getAddress()
            .getStreet())
        .andApplyIfPresent(concat())
        .andApplyIfPresent(concat());

    Optional<String> opt = getter
        .from(new Person("forename", "name", 88, Gender.M, new Address("street", "houseNumber", "zipCode", "city")));
    assertTrue(opt.isPresent());
    assertEquals("streetconcatconcat", opt.get());
  }

  @Test
  public void shouldNestMappings() {
    GetAndApply<Person, String, Integer, RuntimeException> getter = Getter.newFor(Person.class)
        .evaluate(p -> p.getAddress()
            .getStreet())
        .andApplyIfPresent(concat())
        .andApply(lengthOrMinus1());

    Optional<Integer> opt = getter
        .from(new Person("forename", "name", 88, Gender.M, new Address(null, "houseNumber", "zipCode", "city")));
    assertTrue(opt.isPresent());
    assertEquals(-1, (int) opt.get());

    AssertGetter.of(getter)
        .assertGetter(p -> p.getAddress()
            .getStreet())
        .andApplyAlways(lengthOrMinus1());
  }

  private Function<String, String> concat() {
    return str -> str + "concat";
  }

  private Function<String, Integer> lengthOrMinus1() {
    return str -> isNull(str) ? -1 : str.length();
  }

}
