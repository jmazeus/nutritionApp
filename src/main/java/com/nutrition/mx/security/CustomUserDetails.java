package com.nutrition.mx.security;

import java.util.Set;
import java.util.stream.Collectors;

import com.nutrition.mx.enums.Permission;
import com.nutrition.mx.model.User;
import com.nutrition.mx.security.mappers.RolePermissionMapping;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CustomUserDetails {
	private final User user;

	public Set<Permission> getPermissions() {
		return user.getRoles().stream().flatMap(role -> RolePermissionMapping.getPermissionsForRole(role).stream())
				.collect(Collectors.toSet());
	}

}
