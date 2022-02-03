package com.zenika.hexagonal.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class Person {
	
	private PersonIdentifier id;
	
	private String firstname;
	
	private String lastname;
	
	@Builder.Default
	private List<Address> addresses = new ArrayList<>();
	
	public Person withAddressAdded(Address address) {
		var newAddresses = new ArrayList<>(addresses);
		newAddresses.add(address);
		return withNewAddresses(newAddresses);
	}
	
	public Person withAddressRemoved(Address address) {
		var newAddresses = new ArrayList<>(addresses);
		newAddresses.remove(address);
		return withNewAddresses(newAddresses);
	}
	
	public Person withAddressReplaced(Address existing, Address replacement) {
		var newAddresses = addresses.stream().map(address -> existing.equals(address)? replacement : address).collect(Collectors.toList());
		return withNewAddresses(newAddresses);
	}
	
	private Person withNewAddresses(List<Address> newAddresses) {
		return this.toBuilder().addresses(newAddresses).build();
	}
	
	@Value
	public static class PersonIdentifier {
		String value;
	}
}
