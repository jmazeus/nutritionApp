package com.nutrition.mx.model;



import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NutriologoInfo {
    private String cedulaProfesional;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String especialidad;
}

