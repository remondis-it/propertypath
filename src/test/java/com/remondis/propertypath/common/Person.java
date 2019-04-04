package com.remondis.propertypath.common;

public class Person {
  String forename;

  String name;

  int age;

  Gender gender;

  Address address;

  public Person() {
    super();
  }

  public Person(String forename, String name, int age, Gender gender, Address address) {
    super();
    this.forename = forename;
    this.name = name;
    this.age = age;
    this.gender = gender;
    this.address = address;
  }

  public String getForename() {
    return forename;
  }

  public void setForename(String forename) {
    this.forename = forename;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public Gender getGender() {
    return gender;
  }

  public void setGender(Gender gender) {
    this.gender = gender;
  }

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  @Override
  public String toString() {
    return "Person [forename=" + forename + ", name=" + name + ", age=" + age + ", gender=" + gender + "]";
  }

}
