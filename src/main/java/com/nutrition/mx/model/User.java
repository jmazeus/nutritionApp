package com.nutrition.mx.model;

import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nutrition.mx.enums.RoleName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "users")
@Data@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    private String id;
    private String userId;
    private String username;
    private String password;
    private String fullName;
    private String email;
    private String clinicId;
    private List<RoleName> roles;
    private boolean isSuperUser;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private NutriologoInfo nutriologoInfo; // null si no aplica
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private PacienteInfo pacienteInfo;
}

