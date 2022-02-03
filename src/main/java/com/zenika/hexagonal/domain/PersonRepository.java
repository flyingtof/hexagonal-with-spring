package com.zenika.hexagonal.domain;
import java.util.List;

import com.zenika.hexagonal.domain.Person.PersonIdentifier;

public interface PersonRepository {
	
	void save(Person person);
	
	void update(Person person);
	
	Person findById(PersonIdentifier id);
	
	List<Person> getAll();
	
	void remove(PersonIdentifier id);
}
