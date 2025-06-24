package com.nutrition.mx.model;

import java.util.List;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class PacienteProfile {
    @Id
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Valid
    private PacienteInfo pacienteInfo;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<HabitoAlimenticio> habitosAlimenticios;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Antropometria> antropometriaHistorial;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private HistoriaMedica historiaMedica;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<EvaluacionLaboratorio> evaluacionesLaboratorio;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<AnalisisDieta> analisisDieta;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> nutriologosIds; // Historial de nutriólogos
    @NotBlank(message = "El id del nutriólogo es obligatorio")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String nutriologoActualId;    // Nutriólogo actual
}

