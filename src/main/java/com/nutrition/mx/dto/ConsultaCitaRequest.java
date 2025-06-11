package com.nutrition.mx.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ConsultaCitaRequest {

	private String pacienteId;
    private String nutriologoId;
    private LocalDateTime desde;
    private LocalDateTime hasta;
    private String estado;
}
