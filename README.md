[![Maven Central](https://img.shields.io/maven-central/v/com.remondis/propertypath.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.remondis%22%20AND%20a:%22propertypath%22)
[ ![Download](https://api.bintray.com/packages/schuettec/maven/com.remondis.propertypath/images/download.svg) ](https://bintray.com/schuettec/maven/com.remondis.propertypath/_latestVersion)
[![Build Status](https://travis-ci.org/remondis-it/propertypath.svg?branch=develop)](https://travis-ci.org/remondis-it/propertypath)

# Property Path - Simplifying access to Java Bean properties

# Table of Contents
1. [Overview](#overview)
2. [Example](#example)
3. [Supported property paths](#supported-property-paths)
4. [Transforming the result](#transforming-the-result)
5. [Why is my method not supported](#why-is-my-method-not-supported)
6. [How to contribute](#how-to-contribute)
7. [Migration guide](#migration-guide)

# Overview

This library was build to simplify the access of transitive Java Bean properties.

Accessing properties within a tree of Java Beans with optional properties requires many null checks. Every return value of an optional property getter must be null-checked.

Assume the following relationship: `Contract -has an optional company-> Company -has optional list of Addresses-> Address -has City-> String`.

To access the city of the first address you would have to write something like this:

```
  public Optional<String> getCity_showcase_without_PropertyPath() {
    Contract contract = new Contract(...); // Assume we have a contract object
    if (nonNull(contract.getCompany())) {
      List<Address> addresses = contract.getCompany()
          .getAddresses();
      if (nonNull(addresses) && !addresses.isEmpty()) {
        Address address = addresses.get(0);
        return Optional.of(address.getCity());
      }
    }
    return Optional.empty();
  }
```

This library is able to shorten the above example to the following code:

```
    Contract contract = new Contract(...); // Assume we have a contract object
    Optional<String> firstCityOptional = Getter.newFor(Contract.class)
                                               .evaluate(c -> c.getCompany().getAddresses().get(0).getCity())
                                               .from(contract);
```

The property path `c.getCompany().getAddresses().get(0).getCity()` can be declared without writing null-checks. The property path will be evaluated by this library and all neccessary null-checks are performed.
The result is provided by an Optional holding a value if available.

This is supported even for Lists and Maps.


# Example

The following example demonstrates how to generate a sample instance of `Person`.

```
public class Contract {
  private String client;
  private Company company; // Optional, may be null
  // Getters, Setters, No-Args Default Constructor...
}

public class Company {
  private List<Address> addresses; // Optional, may be null or empty
  // Getters, Setters, No-Args Default Constructor...
}

public class Address {
  String street;
  String houseNumber;
  String zipCode;
  String city; // Desired value to get.
  // Getters, Setters, No-Args Default Constructor...
}


@Test
public void shouldGetCityOfFirstAddressNullSafe() {
Optional<String> firstCityOptional = Getter.newFor(Contract.class)
    .evaluate(c -> c.getCompany()
        .getAddresses()
        .get(0)
        .getCity())
    .from(contract);

// Check Optional.isEmpty() for a value and get the value with Optional.get() if present.
}
```

The above example will access the first address of the company's addresses if available. If any get-call returns null or operates on empty lists, the Optional-instance will be empty.

# Supported property paths

When declaring a property path the following calls are supported:
- any Java Bean property conform getter
- java.util.List.get(int)
- java.util.Map.get(Object)

# Transforming the result

Since version `0.0.6` the property path may specify a function that transforms the result value of the property path to a different type.
Due to the fact that a property path may not perform computations, the transform function provides a way to apply computation after evaluating the property path.


## Example

Assume you want to get the length of the street name for a given person you can write the following:
```
Optional<Integer> optStreetLength = Getter.newFor(Person.class)
    .evaluateAnd(p -> p.getAddress()
        .getStreet())
    .apply(String::length)
    .from(person);
```

If the property path evaluates to a non-null value, the transform function (`String::length`) is applied to the result and reduces the street name to the number of characters. The result can be accessed via the returned `java.util.Optional`.

In case the property path evaluates to a null value, the transform function will not be applied.

A transform function may return a null value. In this case an empty `java.util.Optional` is the result.


## Build-in transformations

This library provides some ready-to-use mapping functions - see [here](/com/remondis/propertypath/api/MapTo.java). Currently the following mapping functions can be used:
- `MapTo.nullIfEmpty()`: Maps a string to `null` if the property path returns an empty string.
- `MapTo.nullOr(Function)`: Wraps a mapping function, so that the function is only applied if the result is non-null.
- `MapTo.defaultOr(Function, default)`: Wraps a mapping function, so that a default value is returned if the input is `null`. Otherwise the mapping function is applied.
- `MapTo.emptyXXXIfNull()`: Mapping function that returns an empty collection of the desired type in case the input is `null`.

### Example

Sometimes you want to handle empty strings as an empty result. The mapping function `MapTo.nullIfEmpty()` can be used to build a property path that returns no value in case the string is empty.

```
Person person = new Person("forename", "name", 30, Gender.W, new Address("", "houseNumber", "zipCode", "city"));
GetAndApply<Person, String, String, RuntimeException> get = Getter.newFor(Person.class)
    .evaluate(p -> p.getAddress()
        .getStreet())
    .andApply(MapTo.nullIfEmpty());

Optional<String> opt = get.from(person);
assertFalse(opt.isPresent());
```

In this example the person's address is an empty string. The property path evaluates to no value, because the mapping function was used.

# Why is my method not supported?

This library currently only supports the above method calls in a property path. Other methods are not implemented because this would morph this library into a mocking framework, and that is not the intent.

The method `java.util.Map.getOrDefault(Object, V)` seemed like a good candidate to be implemented and supported in property paths, but using complex objects as key or value in maps introduces a dependency when testing. The arguments in property paths are also checked using equals, so the test must have the exact same arguments which is considered as a strong dependecy.

# How to contribute
Please refer to the project's [contribution guide](CONTRIBUTE.md)



# Migration guide

## Migration from 0.0.x to 0.2.x

The mapping function feature was extended to support two strategies: Apply mapping function
- if value is present
- always

When applying mapping functions with strategy 'always', the mapping function itself can decide whether the property path should return a value, even if the property path does not evaluate to a non-null value.

The assertion API was also extended to specify expectations on the if-present-strategy. Therefore a few changes are necessary when migrating to this version. Basically the `.andApply` should be replaced by `.andApplyIfPresent` (and usages of the assert API accordingly) if your logic requires it.

## Migration from 0.0.x to 0.1.x

Due to a JDK 8 bug related to type inference of generic exception type variables the API was restructured.
You may have to change code if you used the apply-feature but the changes are minimal ;)
