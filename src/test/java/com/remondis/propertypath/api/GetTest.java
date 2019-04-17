package com.remondis.propertypath.api;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.Test;

import com.remondis.propertypath.common.A;
import com.remondis.propertypath.common.Address;
import com.remondis.propertypath.common.BThrowing;
import com.remondis.propertypath.common.C;
import com.remondis.propertypath.common.DummyException;
import com.remondis.propertypath.common.Gender;
import com.remondis.propertypath.common.Person;

public class GetTest {

  @Test
  public void shouldGetFromType() {
    Person person = new Person("forename", "name", 30, Gender.W,
        new Address("street", "houseNumber", "zipCode", "city"));

    Optional<String> streetOpt = Getter.newFor(Person.class)
        .evaluate(p -> p.getAddress()
            .getStreet())
        .from(person);

    assertTrue(streetOpt.isPresent());
    assertEquals("street", streetOpt.get());
  }

  @Test
  public void shouldGetFromTypeSupportExceptions() throws DummyException {
    A a = new A(new BThrowing());
    Get<A, List<C>, DummyException> getCs = Getter.newFor(A.class)
        .evaluateWithException(al -> al.getB()
            .getCs());
    assertThatThrownBy(() -> {
      getCs.from(a);
    }).isInstanceOf(DummyException.class);
  }

}
