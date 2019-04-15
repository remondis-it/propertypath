package com.remondis.propertypath.features.handleNullInput;

import static org.junit.Assert.assertFalse;

import java.util.Optional;

import org.junit.Test;

import com.remondis.propertypath.api.Getter;
import com.remondis.propertypath.common.Person;

public class NullInputTest {
  @Test
  public void shouldReturnDefaultValue() {
    Optional<String> opt = Getter.newFor(Person.class)
        .evaluate(p -> p.getAddress()
            .getCity())
        .from(null);
    assertFalse(opt.isPresent());
  }
}
