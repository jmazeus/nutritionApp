package com.nutrition.mx.validation;

import java.lang.annotation.*;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = ValidUserRoleFieldsValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUserRoleFields {
	String message() default "Campos obligatorios faltantes según el rol";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
