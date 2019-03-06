package com.ddv.test;

import java.util.Optional;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@org.springframework.stereotype.Service
public class Service {

	private static Logger LOGGER = LoggerFactory.getLogger(Service.class);
	
	@Autowired
	private PersonRepository repository;
	
	public Person findPerson(int anId) {
		LOGGER.debug("find person by id {}", anId);
		return search(repository::findById, anId);
	}
	
	private Person search(Function<Integer, Optional<Person>> aFunction, int anId) {
		Delegate delegate = new Delegate();
		return delegate.search(aFunction, anId);
	}
}
