package com.remondis.propertypath.features.exceptions;

import org.junit.Test;

import com.remondis.propertypath.api.Get;
import com.remondis.propertypath.api.Getter;

public class MultipleExceptionsTest {
  @Test
  public void whatHappensIfThereAreDifferenExceptionTypes() throws Exception {
    Get<A, String, Exception> get = Getter.newFor(A.class)
        .evaluateWithException(a -> a.getB()
            .getString());

  }
}
