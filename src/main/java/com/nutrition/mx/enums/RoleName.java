package com.nutrition.mx.enums;

public enum RoleName {
    SUPER_ADMIN,
    CLINIC_ADMIN,
    ESPECIALISTA,
    PACIENTE,
    ASISTENTE;

    public static RoleName fromString(String role) {
        try {
            return RoleName.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Rol inválido: " + role);
        }
    }
}
