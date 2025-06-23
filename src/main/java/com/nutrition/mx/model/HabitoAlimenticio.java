package com.nutrition.mx.model;

import java.util.Date;

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
public class HabitoAlimenticio {
    private int numeroComidasDia;
    private boolean consumeAgua;
    private int litrosAguaDia;
    private String frecuenciaFrutasVerduras;
}

