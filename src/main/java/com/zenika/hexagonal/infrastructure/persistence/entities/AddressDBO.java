package com.zenika.hexagonal.infrastructure.persistence.entities;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.zenika.hexagonal.domain.Address;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "Address")
public class AddressDBO {
	
	@Id
	private String id;
	
	@ManyToOne
	@JoinColumn(name="personId", nullable=false)
	private PersonDBO person;
	
	private String town;
	
	private String state;
	
	private String zipCode;
	
	private String details;
	
	public AddressDBO(Address address, PersonDBO person, int rank) {
		id = person.getId()+rank;
		this.person = person;
		apply(address);
	}
	
	public void apply(Address address) {
		town = address.getTown();
		state = address.getState();
		zipCode = address.getZipCode();
		details = address.getDetails();
	}
	
	public Address toDomain(){
		return Address.builder()
				.state(state)
				.zipCode(zipCode)
				.town(town)
				.details(details)
				.build();
	}
	
}
