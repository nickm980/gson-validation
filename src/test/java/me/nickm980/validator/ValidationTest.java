package me.nickm980.validator;

import org.junit.Test;

import me.nickm.validator.GsonValidator;

public class ValidationTest {

	class BasicObject {
		String name;
		String email;
	}
	
	@Test
	public void test_email_validates() {
		GsonValidator validator = new GsonValidator(new BasicObject());
		validator.validate().toJson();
	}
	
	@Test
	public void test_email_fails_on_invalid_input() {
		
	}
}
