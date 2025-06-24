package com.nutrition.mx.security.mappers;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import com.nutrition.mx.enums.Permission;
import com.nutrition.mx.enums.RoleName;

public class RolePermissionMapping {

    public static Set<Permission> getPermissionsForRole(RoleName role) {
        switch (role) {
            case SUPER_ADMIN:
                return EnumSet.allOf(Permission.class);
            case CLINIC_ADMIN:
                return EnumSet.of(Permission.USUARIOS_CREAR, Permission.USUARIOS_VER, Permission.CITAS_VER, Permission.CLINICA_VER);
            case ESPECIALISTA:
                return EnumSet.of(Permission.CITAS_CREAR, Permission.CITAS_VER, Permission.PACIENTES_VER);
            case ASISTENTE:
                return EnumSet.of(Permission.CITAS_CREAR, Permission.CITAS_VER);
            case PACIENTE:
                return EnumSet.of(Permission.CITAS_VER, Permission.CLINICA_VER);
            default:
                return Collections.emptySet();
        }
    }
}

