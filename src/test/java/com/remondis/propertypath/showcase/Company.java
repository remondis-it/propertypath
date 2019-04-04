package com.remondis.propertypath.showcase;

import java.util.List;

public class Company {

  private List<Address> addresses;

  public Company() {
    super();
  }

  public Company(List<Address> addresses) {
    super();
    this.addresses = addresses;
  }

  public List<Address> getAddresses() {
    return addresses;
  }

  public void setAddresses(List<Address> addresses) {
    this.addresses = addresses;
  }

  @Override
  public String toString() {
    return "Company [addresses=" + addresses + "]";
  }

}
