package com.remondis.propertypath.impl.exceptions;

import static com.remondis.propertypath.impl.InvocationSensor.Invocation.invocationsToString;

import java.util.List;

import com.remondis.propertypath.impl.InvocationSensor.Invocation;

public class NotAValidPropertyPathException extends ReflectionException {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  public NotAValidPropertyPathException() {
  }

  public NotAValidPropertyPathException(String message) {
    super(message);
  }

  public NotAValidPropertyPathException(Throwable cause) {
    super(cause);
  }

  public NotAValidPropertyPathException(String message, Throwable cause) {
    super(message, cause);
  }

  public NotAValidPropertyPathException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public static NotAValidPropertyPathException notAValidPropertyPath(Class<?> sensorType,
      List<Invocation> trackedInvocations) {
    String string = new StringBuilder("The tracked invocations do not select a valid property path: ")
        .append(sensorType.getName())
        .append(".")
        .append(invocationsToString(trackedInvocations))
        .toString();
    return new NotAValidPropertyPathException(string);
  }

}
