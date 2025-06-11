// src/main/java/com/nutrition/mx/controller/ClinicController.java
package com.nutrition.mx.controller;

import com.nutrition.mx.model.Clinic;
import com.nutrition.mx.model.User;
import com.nutrition.mx.repository.UserRepository;
import com.nutrition.mx.response.ClinicResponse;
import com.nutrition.mx.service.ClinicService;
import com.nutrition.mx.utils.SecurityUtils;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clinics")
@RequiredArgsConstructor
public class ClinicController {

	private final ClinicService clinicService;
	private final UserRepository userRepository;

	@PostMapping
	public ResponseEntity<Clinic> createClinic(@Valid @RequestBody Clinic clinic, Authentication authentication) {
		String userId = SecurityUtils.getCurrentUserId();

		User user = userRepository.findByUserId(userId)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

		Clinic savedClinic = clinicService.crearClinica(clinic, user);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedClinic);
	}

	@GetMapping
	public ResponseEntity<ClinicResponse> getClinics(@RequestParam(required = false) String nameClinic,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateInit,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateEnd,
			@RequestParam(required = false) String createdBy, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "createdAt") String sortBy,
			@RequestParam(defaultValue = "desc") String direction) {
		
		User userLogged = new User();
    	String userId = SecurityUtils.getCurrentUserId();
    	String username = SecurityUtils.getCurrentUsername();
    	userLogged.setUsername(username);
    	userLogged.setUserId(userId);
		ClinicResponse response = clinicService.findClinics(nameClinic, dateInit, dateEnd, createdBy, page, size,
				sortBy, direction, userLogged);
		return ResponseEntity.ok(response);
	}
}
