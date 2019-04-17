package com.remondis.propertypath.impl;

import static com.remondis.propertypath.impl.ReflectionUtil.denyNoReturnType;
import static com.remondis.propertypath.impl.ReflectionUtil.findGenericTypeFromMethod;
import static com.remondis.propertypath.impl.ReflectionUtil.isGetterWithArgumentSupport;
import static com.remondis.propertypath.impl.ReflectionUtil.isList;
import static com.remondis.propertypath.impl.ReflectionUtil.isMap;
import static com.remondis.propertypath.impl.ReflectionUtil.nullOrDefaultValue;
import static com.remondis.propertypath.impl.exceptions.ExceptionInPropertyPath.exceptionInPropertyPath;
import static com.remondis.propertypath.impl.exceptions.NotAValidPropertyPathException.notAValidPropertyPath;
import static com.remondis.propertypath.impl.exceptions.ZeroInteractionException.zeroInteractions;
import static java.util.Arrays.asList;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import com.remondis.propertypath.api.PropertyPathWithException;
import com.remondis.propertypath.impl.exceptions.ExceptionInPropertyPath;
import com.remondis.propertypath.impl.exceptions.NotAValidPropertyPathException;
import com.remondis.propertypath.impl.exceptions.ReflectionException;
import com.remondis.propertypath.impl.exceptions.ZeroInteractionException;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InvocationHandler;
import net.sf.cglib.proxy.UndeclaredThrowableException;

public class InvocationSensor<T> {

  public static class Invocation {
    private Method method;
    private Object[] args;
    private Class<?> genericReturnType;

    Invocation(Method method, Object[] args) {
      super();
      this.method = method;
      this.args = args;
    }

    Invocation(Method method, Class<?> genericReturnType, Object[] args) {
      super();
      this.method = method;
      this.genericReturnType = genericReturnType;
      this.args = args;
    }

    public Class<?> getGenericReturnType() {
      return genericReturnType;
    }

    public Method getMethod() {
      return method;
    }

    public Object[] getArgs() {
      return args;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + Arrays.hashCode(args);
      result = prime * result + ((genericReturnType == null) ? 0 : genericReturnType.hashCode());
      result = prime * result + ((method == null) ? 0 : method.hashCode());
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      Invocation other = (Invocation) obj;
      if (!Arrays.equals(args, other.args))
        return false;
      if (genericReturnType == null) {
        if (other.genericReturnType != null)
          return false;
      } else if (!genericReturnType.equals(other.genericReturnType))
        return false;
      if (method == null) {
        if (other.method != null)
          return false;
      } else if (!method.equals(other.method))
        return false;
      return true;
    }

    @Override
    public String toString() {
      return new StringBuilder(getMethod().getName()).append("(")
          .append(Arrays.toString(getArgs())
              .replaceAll("\\[|\\]", ""))
          .append(")")
          .toString();
    }

    public static String invocationsToString(List<Invocation> invocations) {
      StringBuilder b = new StringBuilder();
      Iterator<Invocation> it = invocations.iterator();
      while (it.hasNext()) {
        Invocation invocation = it.next();
        b.append(invocation.getMethod()
            .getName())
            .append("(");
        b.append(Arrays.toString(invocation.getArgs())
            .replaceAll("\\[|\\]", ""))
            .append(")");
        if (it.hasNext()) {
          b.append(".");
        }
      }
      return b.toString();
    }

  }

  private T proxyObject;

  private List<Invocation> invocations = new LinkedList<>();

  private Class<T> superType;

  InvocationSensor(Class<T> superType) {
    this.superType = superType;
  }

  private T createProxy(Class<T> superType, boolean supportTransitiveCalls) {
    Enhancer enhancer = createProxyObject(superType, supportTransitiveCalls);
    return superType.cast(enhancer.create());
  }

  private Enhancer createProxyObject(Class<?> superType, boolean supportTransitiveCalls) {
    Enhancer enhancer = new Enhancer();
    enhancer.setSuperclass(superType);
    enhancer.setCallback(new InvocationHandler() {
      @Override
      public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Invocation invocation = new Invocation(method, args);
        if (isGetterWithArgumentSupport(method)) {
          denyNoReturnType(method);
          // schuettec - Get property name from method and mark this property as called.
          invocations.add(invocation);
          if (supportTransitiveCalls) {
            // schuettec - For getter, return a new enhancer
            Class<?> returnType = method.getReturnType();
            if (isMap(returnType) || isList(returnType)) {
              // if the type is a Map then the desired type is the second type argument
              int typeIndex = isMap(returnType) ? 1 : 0;
              Class<?> genericType = findGenericTypeFromMethod(method, typeIndex);
              Enhancer enhancer = createCollectionProxyObject(returnType, genericType);
              return returnType.cast(enhancer.create());
            } else if (ReflectionUtil.isBean(returnType)) {
              Enhancer enhancer = createProxyObject(returnType, supportTransitiveCalls);
              return returnType.cast(enhancer.create());
            } else {
              return nullOrDefaultValue(method.getReturnType());
            }
          } else {
            return nullOrDefaultValue(method.getReturnType());
          }
        } else {
          throw notAValidPropertyPath(superType, asList(invocation));
        }
      }

    });
    return enhancer;
  }

  private Enhancer createCollectionProxyObject(Class<?> superType, Class<?> genericType) {
    Enhancer enhancer = new Enhancer();
    enhancer.setSuperclass(superType);
    enhancer.setCallback(new InvocationHandler() {
      @Override
      public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Invocation invocation = new Invocation(method, genericType, args);
        if (isGetterWithArgumentSupport(method)) {
          denyNoReturnType(method);
          // schuettec - Get property name from method and mark this property as called.
          invocations.add(invocation);
          // schuettec - For getter, return a new enhancer
          Class<?> returnType = genericType;
          if (isMap(returnType) || isList(returnType)) {
            // if the type is a Map then the desired type is the second type argument
            int typeIndex = isMap(returnType) ? 1 : 0;
            Class<?> genericType = findGenericTypeFromMethod(method, typeIndex);
            Enhancer enhancer = createCollectionProxyObject(returnType, genericType);
            return returnType.cast(enhancer.create());
          } else if (ReflectionUtil.isBean(returnType)) {
            Enhancer enhancer = createProxyObject(returnType, true);
            return returnType.cast(enhancer.create());
          } else {
            return nullOrDefaultValue(method.getReturnType());
          }
        } else {
          throw notAValidPropertyPath(superType, asList(invocation));
        }
      }

    });
    return enhancer;
  }

  /**
   * Returns the proxy object get-method calls can be performed on.
   *
   * @param supportTransitiveCalls
   *
   * @return The proxy.
   */
  private T getSensor(boolean supportTransitiveCalls) {
    proxyObject = createProxy(superType, supportTransitiveCalls);
    return proxyObject;
  }

  /**
   * Returns the list of property names that were tracked by get calls.
   *
   * @return Returns the tracked property names.
   */
  List<Invocation> getTrackedInvocations() {
    return Collections.unmodifiableList(invocations);
  }

  /**
   * Checks if there were any properties accessed by get calls.
   *
   * @return Returns <code>true</code> if there were at least one interaction with a property. Otherwise
   *         <code>false</code> is returned.
   */
  boolean hasTrackedInvocations() {
    return !invocations.isEmpty();
  }

  /**
   * Executes a {@link Function} lambda on a proxy object of the specified type and returns the
   * {@link PropertyDescriptor} of the property selected.
   *
   * @param sensorType
   *        The type of sensor object.
   * @param selector
   *        The selector lambda.
   * @return Returns the {@link PropertyDescriptor} selected by the lambda.
   * @throws ZeroInteractionException Thrown if no interaction was tracked by the field selector.
   * @throws ExceptionInPropertyPath Thrown if a property path fails with an exception.
   * @throws NotAValidPropertyPathException Thrown if the property path contains illegal calls to unsupported methods.
   */
  public static <R, T, E extends Exception> TypedTransitiveProperty<T, R, E> getTransitiveTypedProperty(
      Class<T> sensorType, PropertyPathWithException<R, T, E> selector)
      throws ZeroInteractionException, ExceptionInPropertyPath, NotAValidPropertyPathException {
    InvocationSensor<T> invocationSensor = new InvocationSensor<T>(sensorType);
    T sensor = invocationSensor.getSensor(true);
    // perform the selector lambda on the sensor
    try {
      R returnValue = selector.selectProperty(sensor);

      // if any property interaction was tracked...
      if (invocationSensor.hasTrackedInvocations()) {
        // ...make sure it was exactly one property interaction
        List<Invocation> trackedInvocations = invocationSensor.getTrackedInvocations();
        // Transitively check the tracked invocation and validate if they select a valid
        // property path.
        return TypedTransitiveProperty.of(sensorType, returnValue, trackedInvocations);
      } else {
        throw zeroInteractions(sensorType);
      }
    } catch (UndeclaredThrowableException e) {
      Throwable undeclaredThrowable = e.getUndeclaredThrowable();
      if (undeclaredThrowable instanceof NotAValidPropertyPathException) {
        throw (NotAValidPropertyPathException) undeclaredThrowable;
      } else {
        throw e;
      }
    } catch (ReflectionException e) {
      throw e;
    } catch (Exception exception) {
      throw exceptionInPropertyPath(sensorType, exception);
    }
  }

}
