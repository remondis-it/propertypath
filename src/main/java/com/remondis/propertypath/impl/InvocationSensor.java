package com.remondis.propertypath.impl;

import static com.remondis.propertypath.impl.exceptions.ExceptionInPropertyPath.exceptionInPropertyPath;
import static com.remondis.propertypath.impl.exceptions.ZeroInteractionException.zeroInteractions;
import static java.lang.ClassLoader.getSystemClassLoader;
import static java.util.Objects.isNull;
import static net.bytebuddy.matcher.ElementMatchers.isDeclaredBy;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import com.remondis.propertypath.api.PropertyPath;
import com.remondis.propertypath.impl.exceptions.ExceptionInPropertyPath;
import com.remondis.propertypath.impl.exceptions.NotAValidPropertyPathException;
import com.remondis.propertypath.impl.exceptions.ReflectionException;
import com.remondis.propertypath.impl.exceptions.ZeroInteractionException;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatcher;

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

  private List<Invocation> invocations = new LinkedList<>();

  private T proxyObject;

  private Class<T> superType;

  InvocationSensor(Class<T> superType) {
    this.superType = superType;
  }

  private T createProxy(Class<T> superType, boolean supportTransitiveCalls) {
    return createProxyObject(invocations, superType, supportTransitiveCalls);
  }

  protected static <T> T createProxyObject(List<Invocation> invocations, Class<T> superType,
      boolean supportTransitiveCalls) {
    ClassLoader classLoader;
    if (isNull(superType) || isNull(superType.getClassLoader())) {
      classLoader = getSystemClassLoader();
    } else {
      classLoader = superType.getClassLoader();
    }
    T po = null;
    try {
      po = new ByteBuddy().subclass(superType)
          .method(isDeclaredByClassHierarchy(superType))
          .intercept(MethodDelegation.to(new SingularInterceptor(invocations, supportTransitiveCalls, superType)))
          .make()
          .load(classLoader, ClassLoadingStrategy.Default.INJECTION)
          .getLoaded()
          .getDeclaredConstructor()
          .newInstance();
    } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
      throw new RuntimeException(
          String.format("Error while creating proxy for class '%s'", superType.getCanonicalName()), ex);
    }
    return po;
  }

  protected static Object createCollectionProxyObject(List<Invocation> invocations, Class<?> superType,
      Class<?> genericType) {
    ClassLoader classLoader;
    if (isNull(superType) || isNull(superType.getClassLoader())) {
      classLoader = getSystemClassLoader();
    } else {
      classLoader = superType.getClassLoader();
    }
    Object po = null;
    try {
      po = new ByteBuddy().subclass(superType)
          .method(isDeclaredByClassHierarchy(superType))
          .intercept(MethodDelegation.to(new PluralInterceptor(invocations, superType, genericType)))
          .make()
          .load(classLoader, ClassLoadingStrategy.Default.INJECTION)
          .getLoaded()
          .getDeclaredConstructor()
          .newInstance();
    } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
      throw new RuntimeException(
          String.format("Error while creating proxy for class '%s'", superType.getCanonicalName()), ex);
    }
    return po;
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
      Class<T> sensorType, PropertyPath<R, T, E> selector)
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

  /**
   * Creates an {@link ElementMatcher.Junction} for the method description of all superclasses, interfaces and the given
   * type itself so that all of those methods are proxied by the {@link InvocationSensor}.
   *
   * @param type type to get the junction for
   * @return the junction with all superclasses and interfaces including the given typeD
   */
  private static <T> ElementMatcher.Junction<MethodDescription> isDeclaredByClassHierarchy(Class<T> type) {
    ClassHierarchyIterator classHierarchyIterator = new ClassHierarchyIterator(type);
    ElementMatcher.Junction<MethodDescription> methodDescriptionJunction = null;
    while (classHierarchyIterator.hasNext()) {
      Class<?> next = classHierarchyIterator.next();
      if (isNull(methodDescriptionJunction)) {
        methodDescriptionJunction = isDeclaredBy(next);
      } else {
        methodDescriptionJunction = methodDescriptionJunction.or(isDeclaredBy(next));
      }
    }
    return methodDescriptionJunction;
  }
}
