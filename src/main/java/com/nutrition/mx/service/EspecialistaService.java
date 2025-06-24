package com.nutrition.mx.service;

import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.nutrition.mx.dto.request.CreateUserRequest;
import com.nutrition.mx.model.User;
import com.nutrition.mx.repository.UserRepository;
import com.nutrition.mx.utils.SequenceGeneratorService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EspecialistaService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final SequenceGeneratorService sequenceGeneratorService;

	public ResponseEntity<?> registrarEspecialista(CreateUserRequest request, String currentUsername) {
		String newUserId = sequenceGeneratorService.generateSequence("users_sequence");
		if (request.getEspecialistaInfo() == null || request.getEspecialistaInfo().getCedulaProfesional() == null) {
			return ResponseEntity.badRequest().body("La cédula profesional es obligatoria.");
		}
		if (userRepository
				.findByEspecialistaInfo_CedulaProfesional(request.getEspecialistaInfo().getCedulaProfesional())
				.isPresent()) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body("Ya existe un especialista con la cédula profesional: "
							+ request.getEspecialistaInfo().getCedulaProfesional());
		}
		User especialista = User.builder().userId(newUserId).username(request.getUsername()).email(request.getEmail())
				.password(passwordEncoder.encode(request.getPassword())).roles(request.getRoles())
				.especialistaInfo(request.getEspecialistaInfo()).clinicId(request.getClinicId()).build();
		userRepository.save(especialista);
		return ResponseEntity.status(HttpStatus.CREATED).body("Especialista registrado correctamente.");
	}
}