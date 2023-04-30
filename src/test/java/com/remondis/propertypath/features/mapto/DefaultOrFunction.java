package com.remondis.propertypath.features.mapto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Test;

import com.remondis.propertypath.api.GetAndApply;
import com.remondis.propertypath.api.Getter;
import com.remondis.propertypath.api.MapTo;
import com.remondis.propertypath.common.Gender;
import com.remondis.propertypath.common.Person;

public class DefaultOrFunction {

  @Test
  public void shouldReturnDefault() {
    Person person = new Person("forename", "name", 30, Gender.W, null);

    GetAndApply<Person, String, Integer, RuntimeException> getAndApply = Getter.newFor(Person.class)
        .evaluate(p -> p.getAddress()
            .getStreet())
        .andApply(MapTo.defaultOr(String::length, -1));

    Optional<Integer> opt = getAndApply.from(person);
    assertTrue(opt.isPresent());
    assertEquals(-1, (int) opt.get());
  }

}
