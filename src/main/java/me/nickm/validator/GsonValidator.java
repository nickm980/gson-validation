package me.nickm.validator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class GsonValidator {

	private final static boolean checkAllFieldsForNull = false;
	private Object obj;
	
	public GsonValidator(Object obj) {
		this.obj = obj;
	}
	
	public List<String> validate() {
		List<String> errors = new ArrayList<String>();

		for (Field f : obj.getClass().getDeclaredFields()) {
			f.setAccessible(true);					System.out.println("a");

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
		return errors;
	}
	
	public String validateAndConvertJson(){
		List<String> errors = 
	}
	
	public static String validate(Object obj) {
		GsonValidator validator = new GsonValidator(obj);
		
		return validator.validateAndConvertJson();
	}
}
