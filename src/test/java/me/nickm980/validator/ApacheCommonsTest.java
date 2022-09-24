package me.nickm980.validator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.validator.routines.EmailValidator;
import org.junit.Test;

public class ApacheCommonsTest {

	@Test
	public void test_commons_valid_email_is_valid() {
		assertTrue(EmailValidator.getInstance().isValid("validemail@gmail.com"));
	}
	
	@Test
	public void test_commons_invalid_email_is_invalid() {
		assertFalse(EmailValidator.getInstance().isValid("invalidemail.com"));
	}
}
