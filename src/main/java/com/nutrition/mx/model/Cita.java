package com.nutrition.mx.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "citas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cita {
    @Id
    private String id;
    private String pacienteId;
    private String nutriologoId;
    private LocalDateTime fechaHora;
    private boolean notificarPorCorreo;
    private String agendadaPor;
}

