package com.nutrition.mx.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClinicDto {
    private String id;
    private String name;
    private String address;
    private String createdBy;       // ID del creador (opcional conservarlo)
    private String createdByName;   // Nombre del usuario creador
    private String clinicId;
    private String telephone;
    private String email;
    private String url;
    private Date createdAt;
}

