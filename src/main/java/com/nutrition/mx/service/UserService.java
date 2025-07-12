package com.nutrition.mx.service;

import java.util.List;

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

        public ResponseEntity<?> createSuperAdmin(CreateUserRequest request, String currentUsername) {
                User currentUser = userRepository.findByUsername(currentUsername)
                                .orElseThrow(() -> new UsernameNotFoundException("Usuario actual no encontrado"));
                if (!currentUser.getRoles().contains(RoleName.SUPER_ADMIN)) {
                        throw new AccessDeniedException("No tienes permiso para crear super administradores");
                }
                request.setRoles(List.of(RoleName.SUPER_ADMIN));
                return createBasicUser(request);
        }

        public ResponseEntity<?> createAdmin(CreateUserRequest request, String currentUsername) {
                User currentUser = userRepository.findByUsername(currentUsername)
                                .orElseThrow(() -> new UsernameNotFoundException("Usuario actual no encontrado"));
                if (!currentUser.getRoles().contains(RoleName.SUPER_ADMIN)) {
                        throw new AccessDeniedException("No tienes permiso para crear administradores");
                }
                request.setRoles(List.of(RoleName.CLINIC_ADMIN));
                return createBasicUser(request);
        }

	public ResponseEntity<?> createUser(CreateUserRequest request, String currentUsername) {
		User currentUser = userRepository.findByUsername(currentUsername)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario actual no encontrado"));
                if (containsAdminRole(request.getRoles())) {
                        throw new AccessDeniedException("La creación de administradores se realiza en un endpoint dedicado");
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
                return createBasicUser(request);

	}

        public User createUserInClinic(User user, String clinicId, List<RoleName> roles) {
                user.setClinicId(clinicId);
                user.setRoles(roles);
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                return userRepository.save(user);
        }

        private ResponseEntity<User> createBasicUser(CreateUserRequest request) {
                String newUserId = sequenceGeneratorService.generateSequence("users_sequence");
                User newUser = User.builder()
                                .username(request.getUsername())
                                .password(passwordEncoder.encode(request.getPassword()))
                                .fullName(request.getFullName())
                                .email(request.getEmail())
                                .clinicId(request.getClinicId())
                                .roles(request.getRoles())
                                .especialistaInfo(request.getEspecialistaInfo())
                                .pacienteProfile(request.getPacienteProfile())
                                .userId(newUserId)
                                .build();
                User saved = userRepository.save(newUser);
                return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        }

        private boolean containsAdminRole(List<RoleName> roles) {
                return roles.stream().anyMatch(
                                role -> role == RoleName.SUPER_ADMIN || role == RoleName.CLINIC_ADMIN);
        }

        private boolean existUserEmail (String emailUser) {
                return userRepository.findByEmail(emailUser).isPresent();
        }
	
	private boolean existeUserName (String userName) {
		return userRepository.findByUsername(userName).isPresent();
	}

}
