package com.remondis.propertypath.impl;

import static com.remondis.propertypath.impl.PropertyPathException.accessError;
import static com.remondis.propertypath.impl.ReflectionUtil.isList;
import static com.remondis.propertypath.impl.ReflectionUtil.isMap;
import static com.remondis.propertypath.impl.exceptions.NotAValidPropertyPathException.notAValidPropertyPath;
import static java.util.Objects.isNull;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import com.remondis.propertypath.impl.InvocationSensor.Invocation;
import com.remondis.propertypath.impl.exceptions.NotAValidPropertyPathException;

/**
 *
 * This class provides access to a transitive property path made of a list of {@link PropertyDescriptor}s. The list is
 * sequentially invoked on an object to access the property.
 *
 * @param <T>
 */
class TypedTransitiveProperty<T, R, E extends Exception> {

  private List<Invocation> invocations;
  private Class<T> rootType;

  TypedTransitiveProperty(Class<T> sensorType, R returnValue, List<Invocation> invocations) {
    this.rootType = sensorType;
    this.invocations = invocations;
  }

  @SuppressWarnings("unchecked")
  public R get(T rootTargetInstance) throws E {
    try {
      Object currentTarget = rootTargetInstance;
      Iterator<Invocation> iterator = invocations.iterator();
      while (iterator.hasNext()) {
        Invocation invocation = iterator.next();
        Object[] args = invocation.getArgs();
        Object returnValue = null;
        if (args.length == 0) {
          returnValue = invocation.getMethod()
              .invoke(currentTarget);
        } else {
          try {
            returnValue = invocation.getMethod()
                .invoke(currentTarget, args);
          } catch (InvocationTargetException e) {
            // Hopefully no one will ever find this xD
            if (e.getTargetException() instanceof IndexOutOfBoundsException) {
              return null;
            }
          }
        }
        if (isNull(returnValue)) {
          return null;
        }
        currentTarget = returnValue;
      }
      return (R) currentTarget;
    } catch (InvocationTargetException e) {
      Throwable targetException = e.getTargetException();
      try {
        throw (E) targetException;
      } catch (ClassCastException castException) {
        throw accessError(rootType, invocations, targetException);
      }
    } catch (Exception e) {
      throw accessError(rootType, invocations, e);
    }
  }

  public static <T, R, E extends Exception> TypedTransitiveProperty<T, R, E> of(Class<T> sensorType, R returnValue,
      List<Invocation> trackedInvocations) throws NotAValidPropertyPathException {
    boolean valid = false;
    Class<?> currentType = sensorType;
    Iterator<Invocation> iterator = trackedInvocations.iterator();
    do {
      Invocation invocation = iterator.next();
      // Get properties of the current type
      if (isList(currentType) || isMap(currentType)) {
        valid = invocation.getMethod()
            .getName()
            .equals("get");
        currentType = invocation.getGenericReturnType();
      } else {
        Optional<PropertyDescriptor> property = Properties.getProperties(currentType)
            .stream()
            .filter(pd -> pd.getReadMethod()
                .equals(invocation.getMethod()))
            .findFirst();
        // check, if the current invocation points to a valid property
        valid = property.isPresent();
        if (valid) {
          currentType = property.get()
              .getPropertyType();
        }
      }
      if (!valid) {
        // If the iterator has further invocations, then the property path cannot be
        // valid anymore.
        throw notAValidPropertyPath(sensorType, trackedInvocations);
      }
    } while (iterator.hasNext() && valid);
    return new TypedTransitiveProperty<T, R, E>(sensorType, returnValue, trackedInvocations);
  }

}
