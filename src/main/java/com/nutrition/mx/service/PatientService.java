package com.nutrition.mx.service;

import com.nutrition.mx.dto.request.CreateUserRequest;
import com.nutrition.mx.model.PacienteInfo;
import com.nutrition.mx.model.PacienteProfile;
import com.nutrition.mx.model.User;
import com.nutrition.mx.repository.PacienteProfileRepository;
import com.nutrition.mx.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class PatientService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PacienteProfileRepository pacienteProfileRepository;
    // Otros repos necesarios...

    public ResponseEntity<?> registrarPaciente(CreateUserRequest request, String currentUsername) {
        // Validaciones específicas de paciente (si las hay)
        if (userRepository.findByEmailAndClinicId(request.getEmail(), request.getClinicId()).isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("El usuario ya está registrado en esta clínica.");
        }

        // 1. Crear usuario base
        User pacienteUser = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(request.getPassword())
                .roles(request.getRoles())
                .pacienteProfile(request.getPacienteProfile())
                .build();

        userRepository.save(pacienteUser);

        // 3. (Opcional) lógica adicional, como envío de email de bienvenida, etc.

        return ResponseEntity.ok("Paciente registrado correctamente.");
    }
}
