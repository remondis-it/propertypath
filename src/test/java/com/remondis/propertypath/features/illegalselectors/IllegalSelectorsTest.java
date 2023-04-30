package com.remondis.propertypath.features.illegalselectors;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.remondis.propertypath.api.Getter;
import com.remondis.propertypath.common.Address;
import com.remondis.propertypath.common.Gender;
import com.remondis.propertypath.common.Person;
import com.remondis.propertypath.impl.PropertyPathException;
import org.junit.jupiter.api.Test;

class IllegalSelectorsTest {

  @Test
  void shouldIgnoreValueModification() {
    Person person = new Person("forename", "name", 30, Gender.W,
        new Address("street", "houseNumber", "zipCode", "city"));
    String string = Getter.newFor(Person.class)
        .evaluate(p -> p.getAddress()
            .getStreet() + "_Street")
        .from(person)
        .get();
    assertEquals(string, "street");
  }

  @SuppressWarnings("unlikely-arg-type")
  @Test
  void shouldHandleIllegalSelectorCalls() {
    Person person = new Person("forename", "name", 30, Gender.W,
        new Address("street", "houseNumber", "zipCode", "city"));
    assertThatThrownBy(() -> Getter.newFor(Person.class)
        .evaluate(p -> p.getAddress()
            .equals("Hallo"))
        .from(person)
        .get()).isInstanceOf(PropertyPathException.class)
        .hasMessage("The specified property path contained illegal method calls."
            + " Only getters and calls to List.get(int) and Map.get(Object) are allowed!");
  }

  @Test
  void shouldHandleZeroInteractions() {
    Person person = new Person("forename", "name", 30, Gender.W,
        new Address("street", "houseNumber", "zipCode", "city"));

    assertThatThrownBy(() -> Getter.newFor(Person.class)
        .evaluate(p -> "Hallo")
        .from(person)
        .get()).isInstanceOf(PropertyPathException.class)
        .hasMessage("The specified property path did not interact with the given object.");
  }

  @Test
  void shouldHandleMultipleInteractions() {
    Person person = new Person("forename", "name", 30, Gender.W,
        new Address("street", "houseNumber", "zipCode", "city"));

    assertThatThrownBy(() -> Getter.newFor(Person.class)
        .evaluate(p -> {
          Address address = p.getAddress();
          int age = p.getAge();
          String forename = p.getForename();
          return "Hallo";
        })
        .from(person)
        .get()).isInstanceOf(PropertyPathException.class)
        .hasMessage("The specified property path contained illegal method calls. "
            + "Only getters and calls to List.get(int) and Map.get(Object) are allowed!");
  }

  @Test
  void shouldHandleAccessErrors() {
    Person person = new Person("forename", "name", 30, Gender.W,
        new Address("street", "houseNumber", "zipCode", "city"));

    assertThatThrownBy(() -> Getter.newFor(Person.class)
        .evaluate(p -> {
          throw new RuntimeException("For test!");
        })
        .from(person)
        .get()).isInstanceOf(PropertyPathException.class)
        .hasMessage("The specified property path threw an exception.");
  }

  @Test
  void shouldHandleUnexpectedExceptionsWhileEvaluating() {
    PersonThrowing person = new PersonThrowing("forename", "name", 30, Gender.W,
        new Address("street", "houseNumber", "zipCode", "city"));
    assertThatThrownBy(() -> Getter.newFor(PersonThrowing.class)
        .evaluate(p -> p.getAddress()
            .getCity())
        .from(person)
        .get()).isInstanceOf(PropertyPathException.class)
        .hasMessage(
            "Error while accessing a property 'com.remondis.propertypath.features.illegalselectors.PersonThrowing' with the following property path: PersonThrowing.getAddress().getCity()");
  }
}
