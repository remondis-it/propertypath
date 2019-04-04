package com.remondis.propertypath.features.illegalselectors;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.Test;

import com.remondis.propertypath.api.Getter;
import com.remondis.propertypath.common.Address;
import com.remondis.propertypath.common.Gender;
import com.remondis.propertypath.common.Person;
import com.remondis.propertypath.impl.PropertyPathException;

public class IllegalSelectorsTest {

  @SuppressWarnings("unlikely-arg-type")
  @Test
  public void shouldHandleIllegalSelectorCalls() {
    Person person = new Person("forename", "name", 30, Gender.W,
        new Address("street", "houseNumber", "zipCode", "city"));

    assertThatThrownBy(() -> Getter.from(person)
        .evaluate(p -> p.getAddress()
            .equals("Hallo"))).isInstanceOf(PropertyPathException.class)
                .hasMessage("The specified property path contained illegal method calls."
                    + " Only getters and calls to List.get(int) and Map.get(Object) are allowed!");
  }

  @Test
  public void shouldHandleZeroInteractions() {
    Person person = new Person("forename", "name", 30, Gender.W,
        new Address("street", "houseNumber", "zipCode", "city"));

    assertThatThrownBy(() -> Getter.from(person)
        .evaluate(p -> "Hallo")).isInstanceOf(PropertyPathException.class)
            .hasMessage("The specified property path did not interact with the given object.");
  }

  @Test
  public void shouldHandleMultipleInteractions() {
    Person person = new Person("forename", "name", 30, Gender.W,
        new Address("street", "houseNumber", "zipCode", "city"));

    assertThatThrownBy(() -> Getter.from(person)
        .evaluate(p -> {
          Address address = p.getAddress();
          int age = p.getAge();
          String forename = p.getForename();
          return "Hallo";
        })).isInstanceOf(PropertyPathException.class)
            .hasMessage("The specified property path contained illegal method calls. "
                + "Only getters and calls to List.get(int) and Map.get(Object) are allowed!");
  }

  @Test
  public void shouldHandleAccessErrors() {
    Person person = new Person("forename", "name", 30, Gender.W,
        new Address("street", "houseNumber", "zipCode", "city"));

    assertThatThrownBy(() -> Getter.from(person)
        .evaluate(p -> {
          throw new RuntimeException("For test!");
        })).isInstanceOf(PropertyPathException.class)
            .hasMessage("The specified property path threw an exception.");
  }

  @Test
  public void shouldHandleUnexpectedExceptionsWhileEvaluating() {
    Person person = new PersonThrowing("forename", "name", 30, Gender.W,
        new Address("street", "houseNumber", "zipCode", "city"));
    assertThatThrownBy(() -> Getter.from(person)
        .evaluate(p -> p.getAddress()
            .getCity())).isInstanceOf(PropertyPathException.class)
                .hasMessage(
                    "Error while accessing a property 'com.remondis.propertypath.features.illegalselectors.PersonThrowing' with the following property path: PersonThrowing.getAddress().getCity()");
  }
}
