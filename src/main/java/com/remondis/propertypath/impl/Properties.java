package com.remondis.propertypath.impl;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Util class to get a list of all properties of a class.
 *
 * @author schuettec
 */
class Properties {

  /**
   * Returns a {@link Set} of properties with read and write access.
   *
   * @param inspectType
   *        The type to inspect.
   * @return Returns the list of {@link PropertyDescriptor}s that grant read and write access.
   * @throws MappingException
   *         Thrown on any introspection error.
   */
  static Set<PropertyDescriptor> getProperties(Class<?> inspectType) {
    try {
      BeanInfo beanInfo = Introspector.getBeanInfo(inspectType);
      PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
      return new HashSet<>(Arrays.asList(propertyDescriptors)
          .stream()
          .filter(pd -> !pd.getName()
              .equals("class"))
          .collect(Collectors.toList()));
    } catch (IntrospectionException e) {
      throw new PropertyPathException(String.format("Cannot introspect the type %s.", inspectType.getName()));
    }
  }

}
