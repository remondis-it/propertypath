package com.remondis.propertypath.common;

import java.util.List;
import java.util.Map;

public class B {

  private List<C> cs;
  private Map<String, C> cMap;

  public B() {
    super();
  }

  public B(List<C> cs) {
    super();
    this.cs = cs;
  }

  public B(Map<String, C> cMap) {
    super();
    this.cMap = cMap;
  }

  public Map<String, C> getcMap() {
    return cMap;
  }

  public void setcMap(Map<String, C> cMap) {
    this.cMap = cMap;
  }

  public List<C> getCs() throws DummyException {
    return cs;
  }

  public void setCs(List<C> cs) {
    this.cs = cs;
  }

  @Override
  public String toString() {
    return "B [cs=" + cs + "]";
  }

}
