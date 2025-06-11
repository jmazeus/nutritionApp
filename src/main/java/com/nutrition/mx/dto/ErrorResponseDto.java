// src/main/java/com/nutrition/mx/dto/ErrorResponse.java
package com.nutrition.mx.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

@Data
@AllArgsConstructor
public class ErrorResponseDto {
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
}
