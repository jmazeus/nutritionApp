package com.nutrition.mx.service;

import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nutrition.mx.dto.request.CreateUserRequest;
import com.nutrition.mx.enums.RoleName;
import com.nutrition.mx.model.User;
import com.nutrition.mx.repository.UserRepository;
import com.nutrition.mx.utils.SequenceGeneratorService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final SequenceGeneratorService sequenceGeneratorService;
	private final PatientService patientService;
	private final EspecialistaService especialistaService;

	public ResponseEntity<?> createSuperUser(CreateUserRequest user) {
		User newUser = User.builder().username(user.getUsername()).password(passwordEncoder.encode(user.getPassword()))
				.fullName(user.getFullName()).email(user.getEmail()).roles(user.getRoles()).build();

		User savedUser = userRepository.save(newUser);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
	}

	public ResponseEntity<?> createUser(CreateUserRequest request, String currentUsername) {
		User currentUser = userRepository.findByUsername(currentUsername)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario actual no encontrado"));
		List<RoleName> currentUserRoles = currentUser.getRoles();
		if (containsAdminRole(request.getRoles())
				&& !hasAuthority(currentUserRoles, RoleName.SUPER_ADMIN, RoleName.CLINIC_ADMIN)) {
			throw new AccessDeniedException("No tienes permiso para asignar estos roles");
		}
		
		if(existUserEmail(request.getEmail())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ya existe un registro de usuario con ese email");
		}
		
		if(existeUserName(request.getUsername())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username no disponible");
		}

		if (request.getRoles().contains(RoleName.PACIENTE)) {
			return patientService.registrarPaciente(request, currentUsername);
		}

		if (request.getRoles().contains(RoleName.ESPECIALISTA)) {
			return especialistaService.registrarEspecialista(request, currentUsername);
		}
		String newUserId = sequenceGeneratorService.generateSequence("users_sequence");
		User newUser = User.builder().username(request.getUsername())
				.password(passwordEncoder.encode(request.getPassword())).fullName(request.getFullName())
				.email(request.getEmail()).clinicId(request.getClinicId()).roles(request.getRoles())
				.especialistaInfo(null).pacienteProfile(null).userId(newUserId).build();
		User savedUser = userRepository.save(newUser);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);

	}

	public User createUserInClinic(User user, String clinicId, List<RoleName> roles) {
		user.setClinicId(clinicId);
		user.setRoles(roles);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}

	private boolean containsAdminRole(List<RoleName> roles) {
		return roles.stream().anyMatch(
				role -> role == RoleName.SUPER_ADMIN || role == RoleName.CLINIC_ADMIN || role == RoleName.ESPECIALISTA);
	}

	private boolean hasAuthority(List<RoleName> roles, RoleName... allowedRoles) {
		Set<RoleName> allowed = Set.of(allowedRoles);
		return roles.stream().anyMatch(allowed::contains);
	}
	
	private boolean existUserEmail (String emailUser) {
		return userRepository.findByEmail(emailUser).isPresent();
	}
	
	private boolean existeUserName (String userName) {
		return userRepository.findByUsername(userName).isPresent();
	}

}
