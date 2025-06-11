package com.nutrition.mx.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nutrition.mx.dto.AuthRequest;
import com.nutrition.mx.dto.AuthResponse;
import com.nutrition.mx.dto.request.CreateUserRequest;
import com.nutrition.mx.model.User;
import com.nutrition.mx.repository.UserRepository;
import com.nutrition.mx.security.JwtService;
import com.nutrition.mx.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
	private final UserService userService;
	private final JwtService jwtService;
	private final UserRepository userRepository;
	private final AuthenticationManager authManager;

	@PostMapping("/login")
	public AuthResponse login(@RequestBody AuthRequest request) {
		authManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

		User user = userRepository.findByUsername(request.getUsername()).orElseThrow();
		String jwt = jwtService.generateToken(user);

		return new AuthResponse(jwt);
	}

	@PostMapping("/superuser")
	public ResponseEntity<?> createSuperUser(@RequestBody CreateUserRequest user) {
		return userService.createSuperUser(user);
	}

	@PostMapping("/clinic-user")
	public ResponseEntity<?> createUserForClinic(@RequestParam String clinicId, @RequestBody User user) {
		return ResponseEntity.ok(userService.createUserInClinic(user, clinicId, user.getRoles()));
	}
}
