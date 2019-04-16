package com.remondis.propertypath.features.exceptions;

public class FooException extends Exception {

  public FooException() {
  }

  public FooException(String message) {
    super(message);
  }

  public FooException(Throwable cause) {
    super(cause);
  }

  public FooException(String message, Throwable cause) {
    super(message, cause);
  }

  public FooException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
