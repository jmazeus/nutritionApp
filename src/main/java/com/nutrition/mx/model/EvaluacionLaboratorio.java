package com.nutrition.mx.model;

import java.util.Date;
import java.util.Map;

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
public class EvaluacionLaboratorio {
    private Date fecha;
    private String tipo; // sangre, orina, etc.
    private Map<String, String> resultados; // clave: prueba, valor: resultado
}

