package com.remondis.propertypath.features.maps;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Hashtable;
import java.util.Map;
import java.util.Optional;

import org.junit.Test;

import com.remondis.propertypath.api.AssertGetter;
import com.remondis.propertypath.api.Get;
import com.remondis.propertypath.api.Getter;
import com.remondis.propertypath.common.A;
import com.remondis.propertypath.common.B;
import com.remondis.propertypath.common.C;
import com.remondis.propertypath.common.DummyException;

public class MapTest {

  @Test
  public void shouldSuppotGetOrDefault() throws DummyException {
    Map<String, C> map = new Hashtable<>();
    map.put("one", new C("string1"));
    map.put("two", new C("string2"));
    C c3 = new C("string3");

    A a = new A(new B(map));
    Get<A, String, DummyException> getC3 = Getter.newFor(A.class)
        .evaluate(al -> al.getB()
            .getcMap()
            .getOrDefault("three", c3)
            .getString());
    Optional<String> cOpt3 = getC3.from(a);
    assertTrue(cOpt3.isPresent());
    assertEquals(c3.getString(), cOpt3.get());

    AssertGetter.of(getC3)
        .assertGetter(al -> al.getB()
            .getcMap()
            .getOrDefault("three", c3)
            .getString());
  }

  @Test
  public void shouldHandleNoSuchElementInMap() throws DummyException {
    Map<String, C> map = new Hashtable<>();
    map.put("one", new C("string1"));
    map.put("two", new C("string2"));

    A a = new A(new B(map));
    Get<A, String, DummyException> getC0 = Getter.newFor(A.class)
        .evaluate(al -> al.getB()
            .getcMap()
            .get("DOES NOT EXIST")
            .getString());
    Optional<String> cOpt0 = getC0.from(a);
    assertFalse(cOpt0.isPresent());
  }

  @Test
  public void shouldHandleNullMap() throws DummyException {
    A a = new A(new B());
    Get<A, String, DummyException> getC0 = Getter.newFor(A.class)
        .evaluate(al -> al.getB()
            .getcMap()
            .get("DOES NOT EXIST")
            .getString());
    Optional<String> cOpt0 = getC0.from(a);
    assertFalse(cOpt0.isPresent());
  }

  @Test
  public void shouldGetFromMap() throws DummyException {
    Map<String, C> map = new Hashtable<>();
    map.put("one", new C("string1"));
    map.put("two", new C("string2"));

    A a = new A(new B(map));
    Get<A, String, DummyException> getC0 = Getter.newFor(A.class)
        .evaluate(al -> al.getB()
            .getcMap()
            .get("one")
            .getString());
    Optional<String> cOpt0 = getC0.from(a);
    assertTrue(cOpt0.isPresent());
    assertEquals("string1", cOpt0.get());

    Get<A, String, DummyException> getC1 = Getter.newFor(A.class)
        .evaluate(al -> al.getB()
            .getcMap()
            .get("two")
            .getString());
    Optional<String> cOpt1 = getC1.from(a);
    assertTrue(cOpt1.isPresent());
    assertEquals("string2", cOpt1.get());
  }

}
