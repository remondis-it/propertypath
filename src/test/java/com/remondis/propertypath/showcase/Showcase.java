package com.remondis.propertypath.showcase;

import static java.util.Objects.nonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import com.remondis.propertypath.api.Getter;
import org.junit.jupiter.api.Test;

class Showcase {

  @Test
  void showcase() {

    Address address1 = new Address("street1", "houseNumber1", "zipCode1", "city1");
    Address address2 = new Address("street2", "houseNumber2", "zipCode2", "city2");

    List<Address> addresses = new LinkedList<>();
    addresses.add(address1);
    addresses.add(address2);

    Company company = new Company(addresses);
    Contract contract = new Contract("client", company);

    Optional<String> firstCityOptional = Getter.newFor(Contract.class)
        .evaluate(c -> c.getCompany()
            .getAddresses()
            .get(0)
            .getCity())
        .from(contract);

    // streetOptional is present if the transitive get-chain resolves to a value.
    // If a get call returns null when applied to an object, to Optional would be empty.
    String firstCity = firstCityOptional.get();
    assertEquals(firstCity, address1.getCity());
  }

  public Optional<String> getCity_showcase_without_PropertyPath() {
    Contract contract = new Contract();
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

}
