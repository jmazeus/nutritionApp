// src/main/java/com/nutrition/mx/security/JwtAuthenticationFilter.java
package com.nutrition.mx.security;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nutrition.mx.dto.ErrorResponseDto;
import com.nutrition.mx.enums.Permission;
import com.nutrition.mx.enums.RoleName;
import com.nutrition.mx.security.mappers.RolePermissionMapping;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtService jwtService;
	private final UserDetailsServiceImpl userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		try {
			String authHeader = request.getHeader("Authorization");
			if (authHeader == null || !authHeader.startsWith("Bearer ")) {
				filterChain.doFilter(request, response);
				return;
			}

			String jwt = authHeader.substring(7);
			String username = jwtService.extractUsername(jwt);
			String userId = jwtService.extractUserId(jwt);

			log.info("JWT recibido: {}", jwt);
			log.info("Username extraído del token: {}", username);

			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails = userDetailsService.loadUserByUsername(username);

				if (jwtService.isTokenValid(jwt, username)) {
					List<String> roles = jwtService.extractRoles(jwt);

					Set<Permission> permisos = roles.stream().map(RoleName::valueOf) // Convierte String → RoleName
							.map(RolePermissionMapping::getPermissionsForRole).flatMap(Set::stream)
							.collect(Collectors.toSet());

					// Convertir roles y permisos en authorities
					List<GrantedAuthority> authorities = roles.stream().map(role -> new SimpleGrantedAuthority(role))
							.collect(Collectors.toList());

					authorities.addAll(permisos.stream()
							.map(permission -> new SimpleGrantedAuthority(permission.name())).toList());

					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
							null, authorities);

					authToken.setDetails(new CustomAuthDetails(userId, username));
					log.info("Authorities asignadas: {}", authorities);
					SecurityContextHolder.getContext().setAuthentication(authToken);
				}
			}

			filterChain.doFilter(request, response);
		} catch (ExpiredJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
			ErrorResponseDto errorResponse = new ErrorResponseDto(LocalDateTime.now(),
					HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized", "Token inválido o expirado",
					request.getRequestURI());

			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setContentType("application/json");

			ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());
			response.getWriter().write(mapper.writeValueAsString(errorResponse));
			response.getWriter().flush();
			return;
		}

	}
}
