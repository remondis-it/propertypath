package com.remondis.propertypath.equalityconstraints;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.function.Function;

import org.junit.Test;

import com.remondis.propertypath.api.Get;
import com.remondis.propertypath.api.GetAndApply;
import com.remondis.propertypath.api.Getter;
import com.remondis.propertypath.api.MapTo;
import com.remondis.propertypath.common.Person;

/**
 * Property paths must implement {@link Object#equals(Object)}. Property paths are equal if
 * <ul>
 * <li>the transitive get calls are equal, as well as the used parameters in get calls.</li>
 * <li>use of mapping functions is equal (both use mapping functions or both do not. Note: Equality of the mapping
 * functions cannot be ensured because lambdas are not comparable)</li>
 * <li>if-present strategy is equal</li>
 * <li>TODO: other properties of property paths - to be continued...</li>
 * </ul>
 */
public class EqualityTest {

  @Test
  public void shouldNotBeEqualToGetterAndApply() {

    Get<Person, String, RuntimeException> get = Getter.newFor(Person.class)
        .evaluate(p -> p.getAddress()
            .getStreet());

    Get<Person, String, RuntimeException> getEq = Getter.newFor(Person.class)
        .evaluate(p -> p.getAddress()
            .getStreet());

    GetAndApply<Person, String, Integer, RuntimeException> getAndApply = Getter.newFor(Person.class)
        .evaluate(p -> p.getAddress()
            .getStreet())
        .andApply(lengthOrNull());

    GetAndApply<Person, String, Integer, RuntimeException> getAndApplyEq = Getter.newFor(Person.class)
        .evaluate(p -> p.getAddress()
            .getStreet())
        .andApply(lengthOrNull());

    Get<Person, Integer, RuntimeException> getAndApplyIfPresent = Getter.newFor(Person.class)
        .evaluate(p -> p.getAddress()
            .getStreet())
        .andApplyIfPresent(String::length);

    Get<Person, Integer, RuntimeException> getAndApplyIfPresentEq = Getter.newFor(Person.class)
        .evaluate(p -> p.getAddress()
            .getStreet())
        .andApplyIfPresent(String::length);

    assertTrue(get.equals(getEq));
    assertFalse(get.equals(getAndApply));
    assertFalse(get.equals(getAndApplyIfPresent));

    assertTrue(getAndApply.equals(getAndApplyEq));
    assertFalse(getAndApply.equals(get));
    assertFalse(getAndApply.equals(getAndApplyIfPresent));

    assertTrue(getAndApplyIfPresent.equals(getAndApplyIfPresentEq));
    assertFalse(getAndApplyIfPresent.equals(get));
    assertFalse(getAndApplyIfPresent.equals(getAndApply));

  }

  @Test
  public void shouldNotBeEqualIfPropertyPathChanges() {

    Get<Person, String, RuntimeException> get = Getter.newFor(Person.class)
        .evaluate(p -> p.getAddress()
            .getStreet());

    Get<Person, String, RuntimeException> getNeq = Getter.newFor(Person.class)
        .evaluate(p -> p.getAddress()
            .getHouseNumber());

    GetAndApply<Person, String, Integer, RuntimeException> getAndApply = Getter.newFor(Person.class)
        .evaluate(p -> p.getAddress()
            .getStreet())
        .andApply(lengthOrNull());

    GetAndApply<Person, String, Integer, RuntimeException> getAndApplyNeq = Getter.newFor(Person.class)
        .evaluate(p -> p.getAddress()
            .getHouseNumber())
        .andApply(lengthOrNull());

    Get<Person, Integer, RuntimeException> getAndApplyIfPresent = Getter.newFor(Person.class)
        .evaluate(p -> p.getAddress()
            .getStreet())
        .andApplyIfPresent(String::length);

    Get<Person, Integer, RuntimeException> getAndApplyIfPresentNeq = Getter.newFor(Person.class)
        .evaluate(p -> p.getAddress()
            .getHouseNumber())
        .andApplyIfPresent(String::length);

    assertFalse(get.equals(getNeq));
    assertFalse(getAndApply.equals(getAndApplyNeq));
    assertFalse(getAndApplyIfPresent.equals(getAndApplyIfPresentNeq));

  }

  private Function<String, Integer> lengthOrNull() {
    return MapTo.nullOr(String::length);
  }

}
