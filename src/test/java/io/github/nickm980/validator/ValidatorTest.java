package io.github.nickm980.validator;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.gson.JsonObject;

import io.github.nickm980.validator.JValidate;
import io.github.nickm980.validator.ValidationException;
import io.github.nickm980.validator.JValidate.Email;
import io.github.nickm980.validator.JValidate.Required;
import io.github.nickm980.validator.JValidate.Size;

public class ValidatorTest {

    public class LengthEntity {
	@Size(min = 2, max = 4)
	public int number;

	@Size(min = 0, max = 10)
	public String strNumber;
    }

    public class RequiredFieldEntity {
	@Required
	public String required;

	public String notRequired;
    }

    public class FakeEntity {
	@Email
	public String email;
    }

    @Test
    public void test_email_valid_data() {
	JsonObject obj = new JsonObject();
	obj.addProperty("email", "email@gmail.com");

	FakeEntity entity = JValidate.toValidatedClazz(obj.toString(), FakeEntity.class);

	assertTrue(entity.email.equals("email@gmail.com"));
    }

    @Test
    public void test_email_with_invalid_data() {
	JsonObject obj = new JsonObject();
	obj.addProperty("email", "emailgmail.com");

	ValidationException e = assertThrows(ValidationException.class,
		() -> JValidate.toValidatedClazz(obj.toString(), FakeEntity.class));

	assertTrue(e.getMessages().size() > 0);
    }

    @Test
    public void test_size_valid_data() {
	JsonObject obj = new JsonObject();
	obj.addProperty("number", "3");
	obj.addProperty("strNumber", "2adf4da2");

	LengthEntity entity = JValidate.toValidatedClazz(obj.toString(), LengthEntity.class);

	assertTrue(entity != null);
    }

    @Test
    public void test_size_of_string_with_invalid_data_throws() {
	JsonObject obj = new JsonObject();
	obj.addProperty("strNumber", "thisIsLargerThanTheValue10SoShouldFail");
	obj.addProperty("number", "3");

	ValidationException e = assertThrows(ValidationException.class,
		() -> JValidate.toValidatedClazz(obj.toString(), LengthEntity.class));

	assertTrue(e.getMessages().size() == 1);

    }

    @Test
    public void test_empty_input() {
	JsonObject obj = new JsonObject();
	ValidationException e = assertThrows(ValidationException.class,
		() -> JValidate.toValidatedClazz(obj.toString(), RequiredFieldEntity.class));

	assertTrue(e.getMessages().size() == 1);
    }

    @Test
    public void test_size_of_int_with_invalid_data_throws() {
	JsonObject obj = new JsonObject();
	obj.addProperty("notRequired", "aaa");

	ValidationException e = assertThrows(ValidationException.class,
		() -> JValidate.toValidatedClazz(obj.toString(), LengthEntity.class));

	assertTrue(e.getMessages().size() == 1);
    }

    @Test
    public void test_required_field_with_valid_data() {
	JsonObject obj = new JsonObject();
	obj.addProperty("required", "11233");

	RequiredFieldEntity entity = JValidate.toValidatedClazz(obj.toString(), RequiredFieldEntity.class);

	assertTrue(entity.notRequired == null);
	assertTrue(entity.required != null && !entity.required.isEmpty());
    }

}
