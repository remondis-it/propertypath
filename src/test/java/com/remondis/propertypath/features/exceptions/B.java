package com.remondis.propertypath.features.exceptions;

public class B {
  private String string;

  public B(String string) {
    super();
    this.string = string;
  }

  public B() {
    super();
  }

  public String getString() throws BarException {
    return string;
  }

  public void setString(String string) {
    this.string = string;
  }

}
