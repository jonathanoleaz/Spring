package com.bolsadeindeas.springboot.form.app.validations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = RequeridoRegexValidador.class)
@Retention(RUNTIME)
@Target({ FIELD, METHOD})
public @interface RequeridoRegex {
	
	String message() default "Campo es requerido con anotaciones";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };
    
}