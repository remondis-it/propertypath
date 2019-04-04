package com.remondis.propertypath.common;

import java.util.List;

public class BThrowing extends B {

  @Override
  public List<C> getCs() throws DummyException {
    throw new DummyException("For Test!");
  }
}
