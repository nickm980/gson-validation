package me.nickm.validator;

import java.util.ArrayList;
import java.util.List;

public class ValidationException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private List<InvalidObject> errorMessages;

    public ValidationException(List<InvalidObject> errorMessages, String message) {
	super("Validation Failed");
	this.errorMessages = errorMessages;
    }

    public ValidationException() {
	super();
    }

    public ValidationException(String message) {
	super(message);
    }

    public ValidationException(String message, Throwable cause) {
	super(message, cause);
    }

    public ValidationException(Throwable cause) {
	super(cause);
    }

    public List<String> getFields() {
	List<String> fields = new ArrayList<String>();
	for (InvalidObject obj : errorMessages) {
	    fields.add(obj.getField());
	}
	return fields;
    }

    public List<String> getMessages() {
	List<String> messages = new ArrayList<String>();

	for (InvalidObject obj : errorMessages) {
	    messages.add(obj.getMessage());
	}

	return messages;
    }
}
