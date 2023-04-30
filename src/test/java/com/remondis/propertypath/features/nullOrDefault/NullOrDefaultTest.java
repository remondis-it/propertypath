package com.remondis.propertypath.features.nullOrDefault;

import com.remondis.propertypath.api.Getter;
import com.remondis.propertypath.common.Address;
import com.remondis.propertypath.common.Gender;
import com.remondis.propertypath.common.Person;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class NullOrDefaultTest {

  @Test
  void shouldReturnNullAsDefaultValue() {
    Person person = new Person("forename", "name", 30, Gender.W, null);
    String city = Getter.newFor(Person.class)
        .evaluate(p -> p.getAddress()
            .getCity())
        .fromOrDefault(person, null);
    assertNull(city);
  }

  @Test
  void shouldReturnDefaultValue() {
    Person person = new Person("forename", "name", 30, Gender.W, new Address("street", "houseNumber", "zipCode", null));
    String expectedDefault = "cityDefault";
    String city = Getter.newFor(Person.class)
        .evaluate(p -> p.getAddress()
            .getCity())
        .fromOrDefault(person, expectedDefault);
    assertEquals(expectedDefault, city);
  }

  @Test
  void shouldNotReturnDefaultValue() {
    String expectedCity = "city";
    Person person = new Person("forename", "name", 30, Gender.W,
        new Address("street", "houseNumber", "zipCode", expectedCity));
    String city = Getter.newFor(Person.class)
        .evaluate(p -> p.getAddress()
            .getCity())
        .fromOrDefault(person, "cityDefault");
    assertEquals(expectedCity, city);
  }
}
