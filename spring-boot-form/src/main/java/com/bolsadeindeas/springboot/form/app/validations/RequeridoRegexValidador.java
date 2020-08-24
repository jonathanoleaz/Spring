package com.bolsadeindeas.springboot.form.app.validations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.util.StringUtils;

public class RequeridoRegexValidador implements ConstraintValidator<RequeridoRegex, String>{

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if(value==null || !StringUtils.hasText(value)){
            return false;
        }
        return true;
	}

}