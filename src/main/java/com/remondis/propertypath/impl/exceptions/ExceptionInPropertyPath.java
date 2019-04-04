package com.remondis.propertypath.impl.exceptions;

public class ExceptionInPropertyPath extends ReflectionException {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  private ExceptionInPropertyPath() {
    super();
  }

  private ExceptionInPropertyPath(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  private ExceptionInPropertyPath(String message, Throwable cause) {
    super(message, cause);
  }

  private ExceptionInPropertyPath(String message) {
    super(message);
  }

  private ExceptionInPropertyPath(Throwable cause) {
    super(cause);
  }

  public static ExceptionInPropertyPath exceptionInPropertyPath(Class<?> sensorType, Exception exception) {
    return new ExceptionInPropertyPath(
        String.format("Property path on type '%s' threw an exception.", sensorType.getName()), exception);
  }

}
