# Property Path - Simplifying access to Java Bean properties

# Table of Contents
1. [Overview](#overview)
2. [Example](#example)
3. [How to contribute](#how-to-contribute)

## Overview

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


### Example

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

## Why is ... not supported?

This library currently only supports the above method calls in a property path. Other methods are not implemented because this would morph this library into a mocking framework, and that is not the intend.

The method `java.util.Map.getOrDefault(Object, V)` seemed like a good candidate to be implemented and supported in property paths, but using complex objects as key or value in maps introduces a dependency when testing. The arguments in property paths are also checked using equals, so the test must have the exact same arguments which is considered as a strong dependecy.

# How to contribute
Please refer to the project's [contribution guide](CONTRIBUTE.md)



