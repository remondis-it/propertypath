package com.remondis.propertypath.features.exceptions;

import com.remondis.propertypath.api.Get;
import com.remondis.propertypath.api.Getter;
import org.junit.jupiter.api.Test;

class MultipleExceptionsTest {
  @Test
  void whatHappensIfThereAreDifferenExceptionTypes() {
    Get<A, String, Exception> get = Getter.newFor(A.class)
        .evaluate(a -> a.getB()
            .getString());

  }
}
