package com.bolsadeindeas.springboot.form.app.validations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IdentificadorRegexValidador implements ConstraintValidator<IdentificadorRegex, String>{

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if(value.matches("[0-9]{2}[.][\\d]{3}[.][\\d]{3}[-][a-zA-Z]{1}")){
            return true;
        }
        return false;
	}

}