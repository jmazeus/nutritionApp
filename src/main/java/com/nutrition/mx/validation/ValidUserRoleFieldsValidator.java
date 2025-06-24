package com.nutrition.mx.validation;

import com.nutrition.mx.dto.request.CreateUserRequest;
import com.nutrition.mx.enums.RoleName;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidUserRoleFieldsValidator implements ConstraintValidator<ValidUserRoleFields, CreateUserRequest> {
	@Override
	public boolean isValid(CreateUserRequest value, ConstraintValidatorContext context) {
		if (value == null)
			return true; // other validations will handle null

		boolean isValid = true;

		if (value.getRoles() != null && value.getRoles().contains(RoleName.PACIENTE)) {
			isValid = value.getPacienteProfile() != null;
		}
		if (value.getRoles() != null && value.getRoles().contains(RoleName.ESPECIALISTA)) {
			isValid = value.getEspecialistaInfo() != null;
		}
		return isValid;
	}
}
