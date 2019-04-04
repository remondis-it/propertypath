package com.remondis.propertypath.features.illegalselectors;

import com.remondis.propertypath.common.Address;
import com.remondis.propertypath.common.Gender;
import com.remondis.propertypath.common.Person;

public class PersonThrowing extends Person {

  public PersonThrowing() {
    super();
  }

  public PersonThrowing(String forename, String name, int age, Gender gender, Address address) {
    super(forename, name, age, gender, address);
  }

  @Override
  public Address getAddress() {
    throw new IllegalAccessError("For test!");
  }
}
