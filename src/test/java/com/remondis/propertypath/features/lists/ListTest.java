package com.remondis.propertypath.features.lists;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Test;

import com.remondis.propertypath.api.Get;
import com.remondis.propertypath.api.Getter;
import com.remondis.propertypath.common.A;
import com.remondis.propertypath.common.B;
import com.remondis.propertypath.common.C;
import com.remondis.propertypath.common.DummyException;

public class ListTest {

  @Test
  public void shouldHandleNoSuchElementInList() throws DummyException {
    A a = new A(new B(asList(new C("string1"))));
    Get<A, String, DummyException> getC0 = Getter.newFor(A.class)
        .evaluateWithException(al -> al.getB()
            .getCs()
            .get(99)
            .getString());
    Optional<String> cOpt0 = getC0.from(a);
    assertFalse(cOpt0.isPresent());
  }

  @Test
  public void shouldHandleNullList() throws DummyException {
    A a = new A(new B());
    Get<A, String, DummyException> getC0 = Getter.newFor(A.class)
        .evaluateWithException(al -> al.getB()
            .getCs()
            .get(0)
            .getString());
    Optional<String> cOpt0 = getC0.from(a);
    assertFalse(cOpt0.isPresent());
  }

  @Test
  public void shouldGetFromList() throws DummyException {
    A a = new A(new B(asList(new C("string1"), new C("string2"))));
    Get<A, String, DummyException> getC0 = Getter.newFor(A.class)
        .evaluateWithException(al -> al.getB()
            .getCs()
            .get(0)
            .getString());
    Optional<String> cOpt0 = getC0.from(a);
    assertTrue(cOpt0.isPresent());
    assertEquals("string1", cOpt0.get());

    Get<A, String, DummyException> getC1 = Getter.newFor(A.class)
        .evaluateWithException(al -> al.getB()
            .getCs()
            .get(1)
            .getString());
    Optional<String> cOpt1 = getC1.from(a);
    assertTrue(cOpt1.isPresent());
    assertEquals("string2", cOpt1.get());
  }

}
