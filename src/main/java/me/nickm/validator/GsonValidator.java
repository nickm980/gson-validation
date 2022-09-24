package me.nickm.validator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.validator.ValidatorException;

import com.google.gson.Gson;

public class GsonValidator {
	private final static boolean checkAllFieldsForNull = false;
	private Object obj;

	public GsonValidator(Object obj) {
		this.obj = obj;
	}

	public ValidationBuilder validate() {
		List<String> errors = new ArrayList<String>();

		for (Field f : obj.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			System.out.println("a");

			try {
				if (checkAllFieldsForNull || f.getAnnotation(Required.class) != null) {
					System.out.println("checking");
					if (f.get(obj) == null) {
						errors.add("The field ${" + f.getName() + "} is missing");
					}
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return new ValidationBuilder(errors);
	}

	public class ValidationBuilder {
		List<String> errors;

		public ValidationBuilder(List<String> errors) {
			this.errors = errors;
		}

		public String toJsonOrThrow() throws ValidatorException {
			return "";
		}

		public String toJson() {
			if (!errors.isEmpty()) {
				return null;
			}
			Gson gson = new Gson();
			return gson.toJson(gson);
		}
	}
}
