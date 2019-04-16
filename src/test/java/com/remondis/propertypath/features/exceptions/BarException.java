package com.remondis.propertypath.features.exceptions;

public class BarException extends Exception {

  public BarException() {
  }

  public BarException(String message) {
    super(message);
  }

  public BarException(Throwable cause) {
    super(cause);
  }

  public BarException(String message, Throwable cause) {
    super(message, cause);
  }

  public BarException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
