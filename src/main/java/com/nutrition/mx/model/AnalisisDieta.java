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
public class AnalisisDieta {
    private Date fecha;
    private String descripcion;
    private Map<String, Double> nutrientes; // clave: nutriente, valor: cantidad
}

