package com.remondis.propertypath.showcase;

public class Contract {

  private String client;
  private Company company;

  public Contract() {
    super();
  }

  public Contract(String client, Company company) {
    super();
    this.client = client;
    this.company = company;
  }

  public String getClient() {
    return client;
  }

  public void setClient(String client) {
    this.client = client;
  }

  public Company getCompany() {
    return company;
  }

  public void setCompany(Company company) {
    this.company = company;
  }

  @Override
  public String toString() {
    return "Contract [client=" + client + ", company=" + company + "]";
  }

}
