package com.remondis.propertypath.features.mapto;

import static org.junit.Assert.assertFalse;

import java.util.Optional;

import org.junit.Test;

import com.remondis.propertypath.api.GetAndApply;
import com.remondis.propertypath.api.Getter;
import com.remondis.propertypath.api.MapTo;
import com.remondis.propertypath.common.Address;
import com.remondis.propertypath.common.Gender;
import com.remondis.propertypath.common.Person;

public class NullIfEmpty {

  @Test
  public void shouldReturnNullForEmptyString() {
    Person person = new Person("forename", "name", 30, Gender.W, new Address("", "houseNumber", "zipCode", "city"));

    GetAndApply<Person, String, String, RuntimeException> get = Getter.newFor(Person.class)
        .evaluate(p -> p.getAddress()
            .getStreet())
        .andApply(MapTo.nullIfEmpty());

    Optional<String> opt = get.from(person);
    assertFalse(opt.isPresent());
  }

}
