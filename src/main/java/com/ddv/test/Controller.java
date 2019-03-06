package com.ddv.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

	@Autowired
	private Service service;
	
	@GetMapping("/person")
	public Person findPersonById() {
		return service.findPerson(4);
	}
	
}
