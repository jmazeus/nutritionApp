package com.nutrition.mx.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.nutrition.mx.security.CustomAuthDetails;

public class SecurityUtils {
	public static String getCurrentUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getDetails() instanceof CustomAuthDetails details) {
			return details.getUserId();
		}
		return null;
	}

	public static String getCurrentUsername() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getPrincipal() != null) {
			return authentication.getName();
		}
		return null;
	}
}
