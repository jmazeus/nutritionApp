package com.nutrition.mx.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nutrition.mx.dto.ConsultaCitaRequest;
import com.nutrition.mx.dto.CrearCitaRequest;
import com.nutrition.mx.model.Cita;
import com.nutrition.mx.model.User;
import com.nutrition.mx.service.CitaService;
import com.nutrition.mx.utils.SecurityUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/citas")
@RequiredArgsConstructor
public class CitaController {

    private final CitaService citaService;

    @PostMapping
    public ResponseEntity<?> crearCita(@RequestBody CrearCitaRequest request) {
        try {
        	User userLogged = new User();
        	String userId = SecurityUtils.getCurrentUserId();
        	String username = SecurityUtils.getCurrentUsername();
        	userLogged.setUsername(username);
        	userLogged.setUserId(userId);
            Cita cita = citaService.agendarCita(request, userLogged);
            return ResponseEntity.status(HttpStatus.CREATED).body(cita);
        } catch (Exception e) {
        	log.info("Error al agendar cita " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    
    @GetMapping("/mis-citas")
    public ResponseEntity<List<Cita>> obtenerCitasDelUsuario(@RequestBody ConsultaCitaRequest request) {
    	List<Cita> citas = citaService.obtenerCitasUsuarioAutenticado(request);
        return ResponseEntity.ok(citas);
    }
}

