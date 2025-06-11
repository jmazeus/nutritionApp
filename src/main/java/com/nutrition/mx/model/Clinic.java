package com.nutrition.mx.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "clinics")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Clinic {
    @Id
    private String id;
    @NotNull(message = "El nombre de la clínica es obligatorio")
    @NotEmpty(message = "El nombre de la clínica es obligatorio")
    private String name;
    @NotNull(message = "El domicilio de la clínica es obligatorio")
    @NotEmpty(message = "El domicilio de la clínica es obligatorio")
    private String address;
    private String createdBy;
    private String clinicId;
    @NotNull(message = "Número de contacto de la clínica es obligatorio")
    @NotEmpty(message = "Número de contacto de la clínica es obligatorio")
    private String telephone;
    private String email;
    private String url;
    private Date createdAt;
}

