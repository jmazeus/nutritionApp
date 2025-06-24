package com.nutrition.mx.dto.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nutrition.mx.enums.RoleName;
import com.nutrition.mx.model.EspecialistaInfo;
import com.nutrition.mx.model.PacienteProfile;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;

import com.nutrition.mx.validation.ValidUserRoleFields;

@Data
@ToString
@ValidUserRoleFields
public class CreateUserRequest {

	@NotBlank(message = "El nombre de usuario es obligatorio")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String username;
	@NotBlank(message = "La contraseña es obligatoria")
	@Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String password;
	@NotBlank(message = "El nombre es obligatorio")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String fullName;
	@NotBlank(message = "El correo es obligatorio")
	@Email(message = "El correo debe tener un formato válido")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String email;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String telefono;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String clinicId;
	@NotEmpty(message = "Debe tener al menos un rol")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private List<RoleName> roles;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Boolean isSuperUser;
	@Valid
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private EspecialistaInfo especialistaInfo;
	@Valid
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private PacienteProfile pacienteProfile;
}
