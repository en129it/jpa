package com.ddv.test;

import org.springframework.data.repository.CrudRepository;

public interface PersonRepository extends CrudRepository<Person, Integer> {

	public Person findById(int anId);
}
