package com.nutrition.mx.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nutrition.mx.dto.request.CreateUserRequest;
import com.nutrition.mx.enums.RoleName;
import com.nutrition.mx.model.NutriologoInfo;
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

    public ResponseEntity<?> createSuperUser(CreateUserRequest user) {
        User newUser = User.builder().username(user.getUsername()).password(passwordEncoder.encode(user.getPassword()))
                .fullName(user.getFullName()).email(user.getEmail()).roles(user.getRoles()).build();

        User savedUser = userRepository.save(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    public ResponseEntity<?> createUser(CreateUserRequest request, String currentUsername) {
        boolean esNutriologo = false;
        boolean esPaciente = false;
        NutriologoInfo nutriologoInfo = new NutriologoInfo();
        //PacienteInfo pacienteInfo = new PacienteInfo();

        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario actual no encontrado"));

        List<RoleName> currentUserRoles = currentUser.getRoles();

        if (containsAdminRole(request.getRoles())
                && !hasAuthority(currentUserRoles, RoleName.SUPER_ADMIN, RoleName.CLINIC_ADMIN)) {
            throw new AccessDeniedException("No tienes permiso para asignar estos roles");
        }

        if (request.getRoles().contains(RoleName.NUTRIOLOGO)) {
            esNutriologo = true;
            if (request.getNutriologoInfo() == null || request.getNutriologoInfo().getCedulaProfesional() == null) {
                throw new IllegalArgumentException("La cédula profesional es obligatoria.");
            }

            if (userRepository
                    .findByNutriologoInfo_CedulaProfesional(request.getNutriologoInfo().getCedulaProfesional())
                    .isPresent()) {
                throw new IllegalArgumentException("Ya existe un nutriólogo con la cédula profesional: "
                        + request.getNutriologoInfo().getCedulaProfesional());
            }
        }

        if (request.getRoles().contains(RoleName.PACIENTE)) {
            esPaciente = true;
        }

        // Verifica si ya hay un usuario con el mismo email en la misma clínica
        Optional<User> existingUser = userRepository.findByEmailAndClinicId(request.getEmail(), request.getClinicId());
        if (existingUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Ya existe un usuario con ese correo en la clínica indicada.");
        }

        String newUserId = sequenceGeneratorService.generateSequence("users_sequence");
        if (esNutriologo) {
            nutriologoInfo = NutriologoInfo.builder()
                    .cedulaProfesional(request.getNutriologoInfo().getCedulaProfesional())
                    .especialidad(request.getNutriologoInfo().getEspecialidad()).build();
            User newUser = User.builder().username(request.getUsername())
                    .password(passwordEncoder.encode(request.getPassword())).fullName(request.getFullName())
                    .email(request.getEmail()).clinicId(request.getClinicId()).roles(request.getRoles())
                    .nutriologoInfo(nutriologoInfo).pacienteProfile(request.getPacienteProfile()).userId(newUserId).build();

            User savedUser = userRepository.save(newUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
        }

			/*pacienteInfo = PacienteInfo.builder().estadoSalud(request.getPacienteInfo().getEstadoSalud())
					.fechaNacimiento(request.getPacienteInfo().getFechaNacimiento())
					.genero(request.getPacienteInfo().getGenero()).build();*/
        return patientService.registrarPaciente(request, currentUsername);

    }

    public User createUserInClinic(User user, String clinicId, List<RoleName> roles) {
        user.setClinicId(clinicId);
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    private boolean containsAdminRole(List<RoleName> roles) {
        return roles.stream().anyMatch(
                role -> role == RoleName.SUPER_ADMIN || role == RoleName.CLINIC_ADMIN || role == RoleName.NUTRIOLOGO);
    }

    private boolean hasAuthority(List<RoleName> roles, RoleName... allowedRoles) {
        Set<RoleName> allowed = Set.of(allowedRoles);
        return roles.stream().anyMatch(allowed::contains);
    }

}
