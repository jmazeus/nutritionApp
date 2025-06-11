package com.nutrition.mx.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PacienteInfo {
    private Date fechaNacimiento;
    private String genero;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String estadoSalud;
}
