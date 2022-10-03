package me.nickm.validator;

import java.text.MessageFormat;

public class InvalidObject {

    private final String field;
    private String message;

    public InvalidObject(String field, String message) {
	this.field = field;
	this.message = message;
    }

    public String getField() {
	return field;
    }

    public String getMessage() {
	return MessageFormat.format(message, field);
    }
}
