package com.nutrition.mx.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CrearCitaRequest {
	private LocalDateTime fechaHora;
    private String pacienteId;
    private String nutriologoId;
    private boolean notificarPorCorreo;
}

