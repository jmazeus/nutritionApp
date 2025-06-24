package com.nutrition.mx.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PacienteInfo {
	
	@NotNull(message = "La fecha de nacimiento es obligatoria")
	@JsonInclude(JsonInclude.Include.NON_NULL)
    private Date fechaNacimiento;
	@NotEmpty(message = "Genero obligatorio")
	@JsonInclude(JsonInclude.Include.NON_NULL)
    private String genero;
	@JsonInclude(JsonInclude.Include.NON_NULL)
    private String telefono;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String estadoSalud;
}
