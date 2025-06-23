package com.nutrition.mx.dto.request;

import java.util.List;

import com.nutrition.mx.enums.RoleName;
import com.nutrition.mx.model.NutriologoInfo;
import com.nutrition.mx.model.PacienteInfo;

import com.nutrition.mx.model.PacienteProfile;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CreateUserRequest {
    private String username;
    private String password;
    private String fullName;
    private String email;
    private String clinicId;
    private List<RoleName> roles;
    private Boolean isSuperUser;
    
    private NutriologoInfo nutriologoInfo;
    private PacienteProfile pacienteProfile;
}
