package com.remondis.propertypath.impl;

import static java.util.Objects.nonNull;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Set;

/**
 * Iterator that enables to interate over the complete class hierarchy of a type including superclasses and interfaces.
 */
public class ClassHierarchyIterator implements Iterator<Class<?>> {
  private Queue<Class<?>> remaining = new LinkedList<>();
  private Set<Class<?>> visited = new LinkedHashSet<>();

  public ClassHierarchyIterator(Class<?> initial) {
    append(initial);
  }

  private void append(Class<?> toAppend) {
    if (nonNull(toAppend) && !visited.contains(toAppend)) {
      remaining.add(toAppend);
      visited.add(toAppend);
    }
  }

  @Override
  public boolean hasNext() {
    return !remaining.isEmpty();
  }

  @Override
  public Class<?> next() {
    if (!hasNext()) {
      throw new NoSuchElementException();
    }
    Class<?> polled = remaining.poll();
    append(polled.getSuperclass());
    for (Class<?> superInterface : polled.getInterfaces()) {
      append(superInterface);
    }
    return polled;
  }
}
