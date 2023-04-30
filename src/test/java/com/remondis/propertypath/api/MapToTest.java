package com.remondis.propertypath.api;

import org.junit.jupiter.api.Test;

import static com.remondis.propertypath.api.MapTo.nullIfEmpty;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class MapToTest {
  @Test
  void shouldMapToNull() {
    assertNull(nullIfEmpty().apply(""));
    assertNull(nullIfEmpty().apply(null));
  }

  @Test
  void shouldNotMapToNull() {
    assertNotNull(nullIfEmpty().apply(" "));
    assertNotNull(nullIfEmpty().apply("abc"));
  }

}
