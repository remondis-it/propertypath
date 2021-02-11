package com.remondis.propertypath.features.asString;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.remondis.propertypath.api.Get;
import com.remondis.propertypath.api.Getter;
import com.remondis.propertypath.common.A;
import com.remondis.propertypath.common.B;
import com.remondis.propertypath.common.DummyException;

public class AsStringTest {

  @Test
  public void shouldReturnPropertyPathAsString() {
    Get<A, String, DummyException> getC0 = Getter.newFor(A.class)
        .evaluate(a1 -> a1.getB()
            .getCs()
            .get(99)
            .getString());
    assertEquals("b.cs[99].string", getC0.toPath());
  }

  @Test
  public void shouldReturnPropertyPathAsStringStartWithList() {
    String path = Getter.newFor(B.class)
        .evaluate(b -> b.getCs()
            .get(99)
            .getString())
        .toPath();
    assertEquals("cs[99].string", path);
  }

}
