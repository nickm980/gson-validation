package me.nickm.validator;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.validator.routines.EmailValidator;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Validation utilities for converting json into classes and validating the data
 * against annotations on the class fields
 * 
 * @author nickm980
 *
 */
public final class JValidate {

    private static final Gson gson = new Gson();
    private static HashMap<Class<? extends Annotation>, Command> commands = new HashMap<Class<? extends Annotation>, Command>();

    private JValidate() {
    }

    static {
	registerAnnotation(new EmailCommand(), Email.class);
	registerAnnotation(new SizeCommand(), Size.class);
	registerAnnotation(new RequiredCommand(), Required.class);
    }

    /**
     * Validates the json input and converts it into an object of given class. The
     * json data will check through all validator annotations on the given class and
     * check if they match.
     * 
     * <p>
     * An exception is thrown if the json provides invalid data
     * 
     * @param <T>
     * @param body  The json from which the class is to be serialized
     * @param clazz Class to deserialize into.
     * @return An object of type T from the json data.
     * @throws Exception
     */
    public static <T> T toValidatedClazz(String body, Class<T> clazz) throws ValidationException {
	JsonObject obj = gson.fromJson(body, JsonObject.class);
	List<InvalidObject> invalidFields = new ArrayList<InvalidObject>();

	for (Field field : clazz.getFields()) {
	    for (Class<? extends Annotation> annotation : commands.keySet()) {
		if (!field.isAnnotationPresent(annotation)) {
		    continue;
		}

		Command cmd = commands.get(annotation);
		boolean isValid = cmd.isValid(jsonResult(obj, field.getName()), field);

		if (!isValid) {
		    invalidFields.add(new InvalidObject(field.getName(), cmd.message()));
		}
	    }
	}

	if (invalidFields.size() > 0) {
	    throw new ValidationException(invalidFields, "Some data failed to validate");
	}

	return gson.fromJson(obj.toString(), clazz);
    }

    private static String jsonResult(JsonObject obj, String name) {
	return obj.get(name) == null ? "" : obj.get(name).getAsString();
    }

    /**
     * Register a custom validation check. Must provide a class that implements
     * Command and an annotation. When the annotation is present the provided
     * command will run
     * 
     * @param cmd
     * @param annotation
     */
    public static void registerAnnotation(Command cmd, Class<? extends Annotation> annotation) {
	commands.put(annotation, cmd);
    }

    /**
     * Create a custom command to be run when validating json data.
     * Must register using JValidate.registerAnnotation to enable
     *
     */
    public interface Command {
	/**
	 * Checks if the input is valid
	 * 
	 * @param <A> Annotation type
	 * @param input String value of the input field, taken directly from the json object
	 * @param annotation The annotation which is passed in. The type should be equal to same one
	 * used during registration
	 * @return true if the input is valid, false if invalid
	 */
	<A extends Annotation> boolean isValid(String input, Field field);

	/**
	 * Message that is returned when the data is invalid. 
	 * <p>Can use {0} to insert the field name
	 */
	String message();
    }

    static class EmailCommand implements Command {
	public String message() {
	    return "The value for key {0} is not a valid email";
	}

	public <A extends Annotation> boolean isValid(String input, Field field) {
	    return EmailValidator.getInstance().isValid(input);
	}
    }

    static class RequiredCommand implements Command {
	public String message() {
	    return "The field {0} is required!";
	}

	public <A extends Annotation> boolean isValid(String input, Field field) {
	    return (input != null) && !input.isEmpty();
	}
    }

    static class SizeCommand implements Command {
	public String message() {
	    return "The value {0} is either too small or too large";
	}

	public <A extends Annotation> boolean isValid(String input, Field field) {
	    boolean result = true;
	    Size size = field.getAnnotation(Size.class);
	    
	    if (isNumeric(input)) {
		result = checkSize(size, Integer.parseInt(input));
	    } else {
		result = checkSize(size, input.length());
	    }

	    return result;
	}

	private boolean checkSize(Size size, int length) {
	    boolean result = true;

	    if (length > size.max() || length < size.min()) {
		result = false;
	    }

	    return result;
	}

	private boolean isNumeric(String strNum) {
	    if (strNum == null) {
		return false;
	    }
	    try {
		Integer.parseInt(strNum);
	    } catch (NumberFormatException nfe) {
		return false;
	    }
	    return true;
	}
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface Email {
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface Required {
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface Size {
	/**
	 * Maximum size that the element can be. Defaults to Integer.MAX_VALUE
	 */
	int max() default Integer.MAX_VALUE;

	/**
	 * Minimum size of an element. Defaults to Integer.MIN_VALUE
	 */
	int min() default Integer.MIN_VALUE;
    }

}
