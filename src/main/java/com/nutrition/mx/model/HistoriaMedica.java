package com.nutrition.mx.model;

import java.util.List;

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
public class HistoriaMedica {
    private List<String> enfermedades;
    private List<String> alergias;
    private List<String> medicamentos;
}