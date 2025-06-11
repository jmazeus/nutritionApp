package com.nutrition.mx.security;

import io.jsonwebtoken.security.SignatureException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.nutrition.mx.config.JwtProperties;
import com.nutrition.mx.enums.Permission;
import com.nutrition.mx.enums.RoleName;
import com.nutrition.mx.model.User;
import com.nutrition.mx.security.mappers.RolePermissionMapping;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.nio.charset.StandardCharsets;
import java.security.Key;

@Service
@RequiredArgsConstructor
public class JwtService {

	private final JwtProperties jwtProperties;

	private Key getSigningKey() {
		byte[] keyBytes = jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	public String generateToken(User user) {
		Date now = new Date();
	    Date expirationDate = new Date(now.getTime() + jwtProperties.getExpiration());

	    Map<String, Object> claims = new HashMap<>();
	    List<String> roles = user.getRoles().stream()
	            .map(RoleName::name)
	            .collect(Collectors.toList());
	    Set<Permission> permisos = roles.stream()
	    	    .map(RoleName::valueOf) // Convierte String → RoleName
	    	    .map(RolePermissionMapping::getPermissionsForRole)
	    	    .flatMap(Set::stream)
	    	    .collect(Collectors.toSet());
	    claims.put("authorities", roles);
	    claims.put("userId", user.getUserId());
	    claims.put("permissions", permisos.stream().map(Enum::name).collect(Collectors.toList()));

	    return Jwts.builder()
	            .setClaims(claims)
	            .setSubject(user.getUsername())
	            .setIssuedAt(now)
	            .setExpiration(expirationDate)
	            .signWith(getSigningKey())
	            .compact();
	}

	public String extractUsername(String token) {
		try {
	        return Jwts.parserBuilder()
	                .setSigningKey(getSigningKey())
	                .build()
	                .parseClaimsJws(token)
	                .getBody()
	                .getSubject();
	    } catch (ExpiredJwtException | MalformedJwtException | SignatureException e) {
	        // Puedes agregar logs aquí si deseas
	        throw e; // Propaga la excepción al GlobalExceptionHandler
	    }
	}

	public boolean isTokenValid(String token, String username) {
		String tokenUsername = extractUsername(token);
		return (tokenUsername.equals(username) && !isTokenExpired(token));
	}

	private boolean isTokenExpired(String token) {
		Date expiration = Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody()
				.getExpiration();

		return expiration.before(new Date());
	}
	
	public List<String> extractRoles(String token) {
		Claims claims = extractAllClaims(token);
	    Object authorities = claims.get("authorities");

	    if (authorities instanceof List<?>) {
	        return ((List<?>) authorities).stream()
	                .filter(Objects::nonNull)
	                .map(Object::toString)
	                .collect(Collectors.toList());
	    }

	    return Collections.emptyList();
	}
	
	private Claims extractAllClaims(String token) {
		try {
	        return Jwts.parserBuilder()
	                .setSigningKey(getSigningKey())
	                .build()
	                .parseClaimsJws(token)
	                .getBody();
	    } catch (ExpiredJwtException | MalformedJwtException | SignatureException e) {
	        throw e;
	    }
	}
	
	public String extractUserId(String token) {
	    Claims claims = extractAllClaims(token);
	    return claims.get("userId", String.class);
	}
}
