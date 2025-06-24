package com.nutrition.mx.model;



import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EspecialistaInfo {
	
	@NotBlank(message = "La cédula profesional es oblogatoria")
    private String cedulaProfesional;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotBlank(message = "Especialida obligatoria")
    private String especialidad;
    private int aniosExperiencia;
    private String certificaciones;
}

