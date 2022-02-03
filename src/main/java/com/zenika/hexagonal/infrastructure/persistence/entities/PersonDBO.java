package com.zenika.hexagonal.infrastructure.persistence.entities;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.zenika.hexagonal.domain.Person;
import com.zenika.hexagonal.domain.Person.PersonIdentifier;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class PersonDBO {
	
	@Id
	private String id;
	
	private String firstname;
	
	private String lastname;
	
	@OneToMany(targetEntity = AddressDBO.class, mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<AddressDBO> addresses;
	
	public PersonDBO(Person person) {
		id = person.getId().getValue();
		firstname = person.getFirstname();
		lastname = person.getLastname();
		
		addresses = IntStream.range(0, person.getAddresses().size())
				.boxed().map(rank -> new AddressDBO(person.getAddresses().get(rank), this, rank))
				.collect(Collectors.toList());
	}
	
	public Person toDomain() {
		return Person.builder()
				.id(new PersonIdentifier(id))
				.firstname(firstname)
				.lastname(lastname)
				.addresses(getAddresses().stream()
						.map(AddressDBO::toDomain)
						.collect(Collectors.toList()))
				.build();
	}
	
	public void apply(Person person) {
		this.firstname = person.getFirstname();
		this.lastname = person.getLastname();
		if (person.getAddresses().isEmpty()) {
			this.addresses.clear();
			return;
		}
		var commonListCount = Math.min(person.getAddresses().size(), this.addresses.size());
		for (int i = 0; i < commonListCount; i++) {
			this.addresses.get(i).apply(person.getAddresses().get(i));
		}
		IntStream.range(commonListCount, person.getAddresses().size())
				.boxed().map(rank -> new AddressDBO(person.getAddresses().get(rank), this, rank))
				.forEach(this.addresses::add);
	}
}
