package com.zenika.hexagonal.domain;
import java.util.List;

import com.zenika.hexagonal.domain.Person.PersonIdentifier;

public interface AddressBook {
	
	void createNewPerson(PersonIdentifier id, String firstName, String lastName);
	
	void removePerson(PersonIdentifier id);
	
	void addAddress(PersonIdentifier id, Address address);
	
	void removeAddress(PersonIdentifier id, Address address);
	
	void replaceAddress(PersonIdentifier id, Address existing, Address replacement);
	
	List<Person> getAll();
}
