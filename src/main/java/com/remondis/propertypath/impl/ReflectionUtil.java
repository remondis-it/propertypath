package com.remondis.propertypath.impl;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This is a util class that provides useful reflective methods. <b>Intended for internal use only!</b>.
 *
 * @author schuettec
 */
class ReflectionUtil {

  static final String IS = "is";
  static final String GET = "get";
  static final String SET = "set";

  private static final Set<Class<?>> BUILD_IN_TYPES;
  private static final Map<Class<?>, Object> DEFAULT_VALUES;

  static {
    // schuettec - 08.02.2017 : According to the spec:
    // https://docs.oracle.com/javase/tutorial/java/nutsandbolts/datatypes.html
    Map<Class<?>, Object> map = new HashMap<Class<?>, Object>();
    map.put(boolean.class, false);
    map.put(char.class, '\0');
    map.put(byte.class, (byte) 0);
    map.put(short.class, (short) 0);
    map.put(int.class, 0);
    map.put(long.class, 0L);
    map.put(float.class, 0f);
    map.put(double.class, 0d);
    DEFAULT_VALUES = Collections.unmodifiableMap(map);

    BUILD_IN_TYPES = new HashSet<>();
    BUILD_IN_TYPES.add(Boolean.class);
    BUILD_IN_TYPES.add(Character.class);
    BUILD_IN_TYPES.add(Byte.class);
    BUILD_IN_TYPES.add(Short.class);
    BUILD_IN_TYPES.add(Integer.class);
    BUILD_IN_TYPES.add(Long.class);
    BUILD_IN_TYPES.add(Float.class);
    BUILD_IN_TYPES.add(Double.class);
    BUILD_IN_TYPES.add(String.class);

  }

  private static final Map<String, Class<?>> primitiveNameMap = new HashMap<>();
  private static final Map<Class<?>, Class<?>> wrapperMap = new HashMap<>();

  static {
    primitiveNameMap.put(boolean.class.getName(), boolean.class);
    primitiveNameMap.put(byte.class.getName(), byte.class);
    primitiveNameMap.put(char.class.getName(), char.class);
    primitiveNameMap.put(short.class.getName(), short.class);
    primitiveNameMap.put(int.class.getName(), int.class);
    primitiveNameMap.put(long.class.getName(), long.class);
    primitiveNameMap.put(double.class.getName(), double.class);
    primitiveNameMap.put(float.class.getName(), float.class);
    primitiveNameMap.put(void.class.getName(), void.class);

    wrapperMap.put(boolean.class, Boolean.class);
    wrapperMap.put(byte.class, Byte.class);
    wrapperMap.put(char.class, Character.class);
    wrapperMap.put(short.class, Short.class);
    wrapperMap.put(int.class, Integer.class);
    wrapperMap.put(long.class, Long.class);
    wrapperMap.put(double.class, Double.class);
    wrapperMap.put(float.class, Float.class);
    wrapperMap.put(void.class, Void.class);
  }

  /**
   * Checks if the specified type is a Java build-in type. The build-in types are the object versions of the Java
   * primitives like {@link Integer}, {@link Long} but also {@link String}.
   *
   * @param type
   *        The type to check
   * @return Returns <code>true</code> if the specified type is a java build-in type.
   */
  public static boolean isBuildInType(Class<?> type) {
    return BUILD_IN_TYPES.contains(type);
  }

  /**
   * Returns the default value for the specified primitive type according to the Java Language Specification. See
   * https://docs.oracle.com/javase/tutorial/java/nutsandbolts/datatypes.html for more information.
   *
   * @param type
   *        The type of the primitive.
   * @return Returns the default value of the specified primitive type.
   */
  @SuppressWarnings("unchecked")
  static <T> T defaultValue(Class<T> type) {
    return (T) DEFAULT_VALUES.get(type);
  }

  /**
   * Checks if the method has a return type.
   *
   * @param method
   *        the method
   * @return <code>true</code>, if return type is not {@link Void} or <code>false</code> otherwise.
   */
  static boolean hasReturnType(Method method) {
    return !method.getReturnType()
        .equals(Void.TYPE);
  }

  static boolean isGetterWithArgumentSupport(Method method) {
    boolean isBool = isBoolGetter(method);
    boolean validName = (isBool ? method.getName()
        .startsWith(IS)
        : method.getName()
            .startsWith(GET));
    boolean hasReturnType = hasReturnType(method);
    return validName && hasReturnType;
  }

  /**
   * Finds the generic return type of a method in nested generics. For example this method returns {@link String} when
   * called on a method like <code>List&lt;List&lt;Set&lt;String&gt;&gt;&gt; get();</code>.
   *
   * @param method The method to analyze.
   * @return Returns the inner generic type.
   */
  static Class<?> findGenericTypeFromMethod(Method method, int typeIndex) {
    ParameterizedType parameterizedType = (ParameterizedType) method.getGenericReturnType();
    Type type = null;
    while (parameterizedType != null) {
      type = parameterizedType.getActualTypeArguments()[typeIndex];
      if (type instanceof ParameterizedType) {
        parameterizedType = (ParameterizedType) type;
      } else {
        parameterizedType = null;
      }
    }
    return (Class<?>) type;
  }

  static boolean isBoolGetter(Method method) {
    return isBool(method.getReturnType());
  }

  static boolean isBool(Class<?> type) {
    // isBool is used to determine if "is"-method should be used. This is only the
    // case for primitive type.
    return type == Boolean.TYPE;
  }

  public static boolean isBean(Class<?> type) {
    // TODO: Hier muss erweitert werden.
    try {
      type.getConstructor();
    } catch (NoSuchMethodException e) {
      return false;
    }
    return !isBuildInType(type);
  }

  public static void denyNoReturnType(Method method) {
    if (!hasReturnType(method)) {
      throw PropertyPathException.noReturnTypeOnGetter(method);
    }
  }

  public static Object nullOrDefaultValue(Class<?> returnType) {
    if (returnType.isPrimitive()) {
      return defaultValue(returnType);
    } else {
      return null;
    }
  }

  public static boolean isList(Class<?> returnType) {
    return List.class.isAssignableFrom(returnType);
  }

  public static boolean isMap(Class<?> returnType) {
    return Map.class.isAssignableFrom(returnType);
  }

}
