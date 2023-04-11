package com.remondis.propertypath.impl;

import static com.remondis.propertypath.impl.ReflectionUtil.denyNoReturnType;
import static com.remondis.propertypath.impl.ReflectionUtil.findGenericTypeFromMethod;
import static com.remondis.propertypath.impl.ReflectionUtil.isGetterWithArgumentSupport;
import static com.remondis.propertypath.impl.ReflectionUtil.isList;
import static com.remondis.propertypath.impl.ReflectionUtil.isMap;
import static com.remondis.propertypath.impl.ReflectionUtil.nullOrDefaultValue;
import static com.remondis.propertypath.impl.exceptions.NotAValidPropertyPathException.notAValidPropertyPath;
import static java.util.Arrays.asList;

import java.lang.reflect.Method;
import java.util.List;

import com.remondis.propertypath.impl.InvocationSensor.Invocation;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;

public class PluralInterceptor {

  private List<Invocation> invocations;
  private Class<?> superType;
  private Class<?> genericType;

  public PluralInterceptor(List<Invocation> invocations, Class<?> superType, Class<?> genericType) {
    super();
    this.invocations = invocations;
    this.superType = superType;
    this.genericType = genericType;
  }

  protected List<Invocation> getInvocations() {
    return invocations;
  }

  /**
   * This method will be called each time when the object proxy calls any of its methods.
   *
   * @param method the intercepted method
   * @param args the given arguments to the intercepted method
   * @throws Exception throws
   */
  @RuntimeType
  public Object intercept(@Origin Method method, @AllArguments Object[] args) throws Exception {
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
        return InvocationSensor.createCollectionProxyObject(invocations, returnType, genericType);
      } else if (ReflectionUtil.isBean(returnType)) {
        return InvocationSensor.createProxyObject(invocations, returnType, true);
      } else {
        return nullOrDefaultValue(method.getReturnType());
      }
    } else {
      throw notAValidPropertyPath(superType, asList(invocation));
    }

  }
}
