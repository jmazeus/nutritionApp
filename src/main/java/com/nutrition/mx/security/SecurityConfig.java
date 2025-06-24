package com.nutrition.mx.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.*;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.nutrition.mx.enums.RoleName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

	private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

	private final JwtAuthenticationFilter jwtFilter;
	private final UserDetailsServiceImpl userDetailsService;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	    return http
	        .csrf(csrf -> csrf.disable())
	        .cors(Customizer.withDefaults())
	        .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	        .authorizeHttpRequests(auth -> auth
	            .requestMatchers("/api/auth/**").permitAll()
	            .requestMatchers(HttpMethod.POST, "/api/clinics/**")
	                .hasAuthority(RoleName.SUPER_ADMIN.name())
	            .requestMatchers(HttpMethod.POST, "/api/users/**")
	                .hasAnyAuthority(
	                    RoleName.SUPER_ADMIN.name(),
	                    RoleName.CLINIC_ADMIN.name(),
	                    RoleName.ESPECIALISTA.name()
	                )
	                .requestMatchers(HttpMethod.POST, "/api/citas/**")
	                .hasAnyAuthority(
	                		RoleName.CLINIC_ADMIN.name(),
	                		RoleName.ESPECIALISTA.name(),
	                		RoleName.ASISTENTE.name())
	            .anyRequest().authenticated()
	        )
	        .userDetailsService(userDetailsService)
	        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
	        .build();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		log.info("config: " + config);
		return config.getAuthenticationManager();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
