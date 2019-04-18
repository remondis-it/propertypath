package com.remondis.propertypath.features.handleNull;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Test;

import com.remondis.propertypath.api.Get;
import com.remondis.propertypath.api.Getter;
import com.remondis.propertypath.common.A;
import com.remondis.propertypath.common.B;
import com.remondis.propertypath.common.C;
import com.remondis.propertypath.common.DummyException;

public class HandleNullTest {

  @Test
  public void shouldHandleNullValue_1() throws DummyException {
    A a = new A();
    Get<A, String, DummyException> getC0 = Getter.newFor(A.class)
        .evaluate(al -> al.getB()
            .getCs()
            .get(0)
            .getString());
    Optional<String> cOpt0 = getC0.from(a);
    assertFalse(cOpt0.isPresent());
  }

  @Test
  public void shouldHandleNullValue_2() throws DummyException {
    A a = new A(new B());
    Get<A, String, DummyException> getC0 = Getter.newFor(A.class)
        .evaluate(al -> al.getB()
            .getCs()
            .get(0)
            .getString());
    Optional<String> cOpt0 = getC0.from(a);
    assertFalse(cOpt0.isPresent());
  }

  @Test
  public void shouldHandleNullValue_3() throws DummyException {
    A a = new A(new B(asList(null, new C("string1"))));
    Get<A, String, DummyException> getC0 = Getter.newFor(A.class)
        .evaluate(al -> al.getB()
            .getCs()
            .get(0)
            .getString());
    Optional<String> cOpt0 = getC0.from(a);
    assertFalse(cOpt0.isPresent());
  }

  @Test
  public void shouldHandleNullValue_4() throws DummyException {
    A a = new A(new B(asList(null, new C(null))));
    Get<A, String, DummyException> getC0 = Getter.newFor(A.class)
        .evaluate(al -> al.getB()
            .getCs()
            .get(1)
            .getString());
    Optional<String> cOpt0 = getC0.from(a);
    assertFalse(cOpt0.isPresent());
  }

  @Test
  public void shouldGetValue() throws DummyException {
    String expectedValue = "string1";
    A a = new A(new B(asList(null, new C(expectedValue))));
    Get<A, String, DummyException> getC0 = Getter.newFor(A.class)
        .evaluate(al -> al.getB()
            .getCs()
            .get(1)
            .getString());
    Optional<String> cOpt0 = getC0.from(a);
    assertTrue(cOpt0.isPresent());
    assertSame(expectedValue, cOpt0.get());
  }

}
