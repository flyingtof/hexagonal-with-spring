package com.zenika.hexagonal.infrastructure;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.zenika.hexagonal.domain.Address;
import com.zenika.hexagonal.domain.Person;
import com.zenika.hexagonal.domain.Person.PersonIdentifier;

@SpringBootTest
class TransactionalAddressBookTest {
	
	private @Autowired
	TransactionalAddressBook personManager;
	
	
	@BeforeEach
	void cleanup(){
		personManager.getAll().forEach(person -> personManager.removePerson(person.getId()));
	}
	
	@Test
	void testAddPerson(){
		// Given
		var id = new PersonIdentifier("1234");
		var firstname = "first";
		var lastname = "last";
		
		// When
		personManager.createNewPerson(id, firstname, lastname);
		
		// Then
		assertHas(Person.builder().id(id).firstname(firstname).lastname(lastname).build());
	}
	
	@Test
	void testAddAddress(){
		// Given
		var id = new PersonIdentifier("1234");
		var firstname = "first";
		var lastname = "last";
		personManager.createNewPerson(id, firstname, lastname);
		var address = Address.builder()
				.zipCode("zipcode")
				.details("details")
				.state("state")
				.town("town")
				.build();
		
		// When
		personManager.addAddress(id,address);
		
		// Then
		var person = personManager.getAll().get(0);
		assertHas(Person.builder()
				.id(id).firstname(firstname).lastname(lastname).addresses(List.of(address))
				.build());
	}
	
	@Test
	void testDeleteAddress(){
		// Given
		var id = new PersonIdentifier("1234");
		var firstname = "first";
		var lastname = "last";
		personManager.createNewPerson(id, firstname, lastname);
		var address = Address.builder()
				.zipCode("zipcode")
				.details("details")
				.state("state")
				.town("town")
				.build();
		personManager.addAddress(id, address);
		
		// When
		personManager.removeAddress(id, address);
		
		// Then
		assertHas(Person.builder().id(id).firstname(firstname).lastname(lastname).addresses(List.of()).build());
	}
	
	@Test
	void testReplaceAddress(){
		// Given
		var id = new PersonIdentifier("1234");
		var firstname = "first";
		var lastname = "last";
		personManager.createNewPerson(id, firstname, lastname);
		var address = Address.builder()
				.zipCode("zipcode")
				.details("details")
				.state("state")
				.town("town")
				.build();
		personManager.addAddress(id, address);

		// When
		var newAddress = Address.builder()
				.zipCode("zipcode")
				.details("new details")
				.state("state")
				.town("town")
				.build();
		personManager.replaceAddress(id, address, newAddress);
		
		// Then
		
		assertHas(Person.builder()
				.id(id).firstname(firstname).lastname(lastname).addresses(List.of(newAddress))
				.build());
	}
	
	private void assertHas(Person expectedPerson) {
		assertThat(personManager.getAll().stream().filter(expectedPerson::equals).count()).isEqualTo(1);
	}
	
}