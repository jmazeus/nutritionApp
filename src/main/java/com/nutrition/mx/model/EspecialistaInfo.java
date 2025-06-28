package com.nutrition.mx.model;



import com.fasterxml.jackson.annotation.JsonInclude;
import com.nutrition.mx.validation.ValidUserRoleFields;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ValidUserRoleFields
public class EspecialistaInfo {
	
	@NotBlank(message = "La cédula profesional es oblogatoria")
    private String cedulaProfesional;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotBlank(message = "Especialida obligatoria")
    private String especialidad;
    @Min(0)
    private Integer aniosExperiencia;
    private String certificaciones;
}

