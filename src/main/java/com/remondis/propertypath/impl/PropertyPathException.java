package com.remondis.propertypath.impl;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

import com.remondis.propertypath.impl.InvocationSensor.Invocation;

/**
 * Thrown if the mapping configuration has errors or if an actual mapping fails.
 */
public class PropertyPathException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  PropertyPathException() {
    super();
  }

  PropertyPathException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  PropertyPathException(String message, Throwable cause) {
    super(message, cause);
  }

  PropertyPathException(String message) {
    super(message);
  }

  PropertyPathException(Throwable cause) {
    super(cause);
  }

  static PropertyPathException notAProperty(Class<?> type, String property) {
    return new PropertyPathException(String.format(
        "The get-method for property '%s' in type %s is not a valid Java Bean property.", property, type.getName()));
  }

  static PropertyPathException noReturnTypeOnGetter(Method method) {
    return new PropertyPathException(
        String.format("The method '%s' in type '%s' is not a valid getter because it has no return type.",
            method.getName(), method.getDeclaringClass()
                .getName()));
  }

  static PropertyPathException noDefaultConstructor(Class<?> type, Exception e) {
    return new PropertyPathException(String.format(
        "The type %s does not have a public no-args constructor and cannot be used for mapping.", type.getName()), e);
  }

  static PropertyPathException newInstanceFailed(Class<?> type, Exception e) {
    return new PropertyPathException(String.format("Creating a new instance of type %s failed.", type.getName()), e);
  }

  static PropertyPathException unsupportedCollection(Collection<?> collection) {
    return new PropertyPathException(String.format(
        "The collection '%s' is currently not supported. Only java.util.Set and java.util.List"
            + " are supported collections.",
        collection.getClass()
            .getName()));
  }

  static PropertyPathException unsupportedCollection(Class<?> collectionType) {
    return new PropertyPathException(String.format("The collection type %s is unsupported.", collectionType.getName()));
  }

  static PropertyPathException accessError(Class<?> sensorType, List<Invocation> invocations, Throwable e) {
    StringBuilder b = new StringBuilder("Error while accessing a property '").append(sensorType.getName())
        .append("' with the following property path: ")
        .append(sensorType.getSimpleName())
        .append(".")
        .append(Invocation.invocationsToString(invocations));
    return new PropertyPathException(b.toString(), e);
  }

}
