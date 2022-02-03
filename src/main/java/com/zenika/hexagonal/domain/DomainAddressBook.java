package com.zenika.hexagonal.domain;
import java.util.List;

import org.springframework.stereotype.Service;

import com.zenika.hexagonal.domain.Person.PersonIdentifier;

@Service
public class DomainAddressBook implements AddressBook {
	
	private final PersonRepository personRepository;
	
	public DomainAddressBook(PersonRepository personRepository) {
		this.personRepository = personRepository;
	}
	
	@Override
	public void createNewPerson(PersonIdentifier id, String firstName, String lastName) {
		personRepository.save(Person.builder().id(id).firstname(firstName).lastname(lastName).build());
	}
	
	@Override
	public void removePerson(PersonIdentifier id) {
		personRepository.remove(id);
	}
	
	@Override
	public void addAddress(PersonIdentifier id, Address address) {
		var person = personRepository.findById(id);
		personRepository.update(person.withAddressAdded(address));
	}
	
	@Override
	public void removeAddress(PersonIdentifier id, Address address) {
		var person = personRepository.findById(id);
		personRepository.update(person.withAddressRemoved(address));
	}
	
	@Override
	public void replaceAddress(PersonIdentifier id, Address existing, Address replacement) {
		var person = personRepository.findById(id);
		personRepository.update(person.withAddressReplaced(existing, replacement));
	}
	
	@Override
	public List<Person> getAll() {
		return personRepository.getAll();
	}
}
