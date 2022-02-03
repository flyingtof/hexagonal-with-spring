package com.zenika.hexagonal.infrastructure.persistence.repositories;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.zenika.hexagonal.domain.Person;
import com.zenika.hexagonal.domain.Person.PersonIdentifier;
import com.zenika.hexagonal.domain.PersonRepository;
import com.zenika.hexagonal.infrastructure.persistence.entities.PersonDBO;

@Repository
public class JpaPersonRepository implements PersonRepository {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public void save(Person person) {
		entityManager.persist(new PersonDBO(person));
	}
	
	@Override
	public void update(Person person) {
		var existingEntity = entityManager.find(PersonDBO.class, person.getId().getValue());
		existingEntity.apply(person);
		entityManager.merge(existingEntity);
	}
	
	@Override
	public Person findById(PersonIdentifier id) {
		return entityManager
				.createQuery("select p from PersonDBO p left join fetch p.addresses where p.id = :id", PersonDBO.class)
				.setParameter("id", id.getValue())
				.getSingleResult()
				.toDomain();
	}
	
	@Override
	public List<Person> getAll() {
		return entityManager
				.createQuery("select p from PersonDBO p left join fetch p.addresses", PersonDBO.class)
				.getResultList().stream().map(PersonDBO::toDomain)
				.collect(Collectors.toList());
	}
	
	@Override
	public void remove(PersonIdentifier id) {
		entityManager.remove(entityManager.find(PersonDBO.class, id.getValue()));
	}
}
