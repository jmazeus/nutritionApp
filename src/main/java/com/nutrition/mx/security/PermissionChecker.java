package com.nutrition.mx.security;

import java.util.Set;

import org.springframework.security.access.AccessDeniedException;

import com.nutrition.mx.enums.Permission;
import com.nutrition.mx.model.User;
import com.nutrition.mx.security.mappers.RolePermissionMapping;

public class PermissionChecker {

    public static boolean tienePermiso(User user, Permission permisoRequerido) {
        return user.getRoles().stream()
                .map(RolePermissionMapping::getPermissionsForRole)
                .flatMap(Set::stream)
                .anyMatch(p -> p.equals(permisoRequerido));
    }

    public static void verificarPermiso(User user, Permission permisoRequerido) {
        if (!tienePermiso(user, permisoRequerido)) {
            throw new AccessDeniedException("No tienes permiso para realizar esta acción.");
        }
    }
}

