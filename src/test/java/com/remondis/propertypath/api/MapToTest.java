package com.remondis.propertypath.api;

import static com.remondis.propertypath.api.MapTo.nullIfEmpty;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class MapToTest {
  @Test
  public void shouldMapToNull() {
    assertNull(nullIfEmpty().apply(""));
    assertNull(nullIfEmpty().apply(null));
  }

  @Test
  public void shouldNotMapToNull() {
    assertNotNull(nullIfEmpty().apply(" "));
    assertNotNull(nullIfEmpty().apply("abc"));
  }

}
