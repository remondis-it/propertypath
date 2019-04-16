package com.remondis.propertypath.features.exceptions;

public class A {
  private B b;

  public A(B b) {
    super();
    this.b = b;
  }

  public A() {
    super();
  }

  public B getB() throws FooException {
    return b;
  }

  public void setB(B b) {
    this.b = b;
  }

}
