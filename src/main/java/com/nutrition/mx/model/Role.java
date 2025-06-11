package com.nutrition.mx.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.nutrition.mx.enums.RoleName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {
    @Id
    private String id;
    private RoleName name; // e.g., "ADMIN", "NUTRICIONISTA", "PACIENTE"
    private String clinicId;
}
