package com.nutrition.mx.service;

import com.nutrition.mx.dto.request.CreateUserRequest;
import com.nutrition.mx.model.User;
import com.nutrition.mx.repository.UserRepository;
import com.nutrition.mx.utils.SequenceGeneratorService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PatientService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final SequenceGeneratorService sequenceGeneratorService;

	public ResponseEntity<?> registrarPaciente(CreateUserRequest request, String currentUsername) {
		
		if (userRepository.findByEmailAndClinicId(request.getEmail(), request.getClinicId()).isPresent()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El usuario ya está registrado en esta clínica.");
		}
		String newUserId = sequenceGeneratorService.generateSequence("users_sequence");
		User pacienteUser = User.builder().username(request.getUsername()).email(request.getEmail())
				.password(passwordEncoder.encode(request.getPassword())).roles(request.getRoles())
				.pacienteProfile(request.getPacienteProfile()).userId(newUserId).build();

		userRepository.save(pacienteUser);
		return ResponseEntity.ok("Paciente registrado correctamente.");
	}
}
