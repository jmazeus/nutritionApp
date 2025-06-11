package com.nutrition.mx.model;

import java.util.Date;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Antropometria {
    private Date fecha;
    private double peso;
    private double estatura;
    private double imc;
    private Map<String, Double> otrosValores; // Por ejemplo cintura, grasa corporal, etc.
}

