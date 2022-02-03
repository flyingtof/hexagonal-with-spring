package com.zenika.hexagonal.infrastructure.web;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zenika.hexagonal.domain.Address;
import com.zenika.hexagonal.domain.Person;
import com.zenika.hexagonal.domain.Person.PersonIdentifier;
import com.zenika.hexagonal.domain.AddressBook;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(path = "/person")
@Slf4j
public class PersonController {
	
	private final AddressBook addressBook;
	
	public PersonController(AddressBook addressBook) {
		this.addressBook = addressBook;
	}
	
	@GetMapping
	public List<Person> getAll() {
		log.info("start getAll()");
		var all = addressBook.getAll();
		log.info("done getAll()");
		return all;
	}
	
	@PostMapping
	public void createPerson(@RequestBody PersonDto person) {
		addressBook.createNewPerson(new PersonIdentifier(person.getId()), person.getFirstName(), person.getLastName());
	}
	
	@PostMapping(path = "/{personId}/address")
	public void addAddress(@PathVariable String personId, @RequestBody AddressDto address) {
		addressBook.addAddress(new PersonIdentifier(personId), address.toDomain());
	}
	
	@Data
	public static class PersonDto {
		
		private String id;
		
		private String firstName;
		
		private String lastName;
	}
	
	@Data
	public static class AddressDto {
		
		private String town;
		
		private String state;
		
		private String zipCode;
		
		private String details;
		
		public Address toDomain() {
			return Address.builder()
					.town(town).state(state).details(details).zipCode(zipCode)
					.build();
		}
	}
}
