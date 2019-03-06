package com.ddv.test;

import java.util.Optional;
import java.util.function.Function;

public class Delegate {

	public Person search(Function<Integer, Optional<Person>> aFunction, int anId) {
		Optional<Person> rslt =  aFunction.apply(anId);
		return rslt.isPresent() ? rslt.get() : null;
	}
	
}
