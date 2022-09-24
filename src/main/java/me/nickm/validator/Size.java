package me.nickm.validator;

public @interface Size {
	int max() default Integer.MIN_VALUE;
	int min() default Integer.MAX_VALUE;
}
