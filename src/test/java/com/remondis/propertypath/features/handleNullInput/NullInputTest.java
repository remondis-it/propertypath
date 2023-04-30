package com.remondis.propertypath.features.handleNullInput;

import java.util.Optional;

import com.remondis.propertypath.api.Getter;
import com.remondis.propertypath.common.Person;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

class NullInputTest {
  @Test
  void shouldReturnDefaultValue() {
    Optional<String> opt = Getter.newFor(Person.class)
        .evaluate(p -> p.getAddress()
            .getCity())
        .from(null);
    assertFalse(opt.isPresent());
  }
}
