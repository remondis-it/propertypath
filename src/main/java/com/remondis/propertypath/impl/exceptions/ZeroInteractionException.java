package com.remondis.propertypath.impl.exceptions;

public class ZeroInteractionException extends ReflectionException {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  public ZeroInteractionException() {
  }

  public ZeroInteractionException(String message) {
    super(message);
  }

  public ZeroInteractionException(Throwable cause) {
    super(cause);
  }

  public ZeroInteractionException(String message, Throwable cause) {
    super(message, cause);
  }

  public ZeroInteractionException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public static ZeroInteractionException zeroInteractions(Class<?> sensorType) {
    return new ZeroInteractionException(String
        .format("There were zero interactions with the property selector applied on type %s.", sensorType.getName()));
  }

}
