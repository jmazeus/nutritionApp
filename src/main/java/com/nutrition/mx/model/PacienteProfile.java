package com.nutrition.mx.model;

import java.util.List;

import org.springframework.data.annotation.Id;

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
    private String id;
    private PacienteInfo pacienteInfo;
    private List<HabitoAlimenticio> habitosAlimenticios;
    private List<Antropometria> antropometriaHistorial;
    private HistoriaMedica historiaMedica;
    private List<EvaluacionLaboratorio> evaluacionesLaboratorio;
    private List<AnalisisDieta> analisisDieta;
    private List<String> nutriologosIds; // Historial de nutriólogos
    private String nutriologoActualId;    // Nutriólogo actual
}

