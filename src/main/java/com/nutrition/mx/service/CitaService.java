package com.nutrition.mx.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.nutrition.mx.dto.ConsultaCitaRequest;
import com.nutrition.mx.dto.CrearCitaRequest;
import com.nutrition.mx.enums.Permission;
import com.nutrition.mx.enums.RoleName;
import com.nutrition.mx.model.Cita;
import com.nutrition.mx.model.User;
import com.nutrition.mx.repository.CitaRepository;
import com.nutrition.mx.repository.UserRepository;
import com.nutrition.mx.security.mappers.RolePermissionMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CitaService {

	private final CitaRepository citaRepository;
	private final UserRepository userRepository;
	private final EmailService emailService;

	public Cita agendarCita(CrearCitaRequest request, User userLogged) {
		User usuarioQueAgenda = userRepository.findByUserId(userLogged.getUserId())
				.orElseThrow(() -> new UsernameNotFoundException("Usuario que agenda no encontrado"));

		boolean esNutriologo = usuarioQueAgenda.getRoles().contains(RoleName.ESPECIALISTA);
		String nutriologoId;
		if (esNutriologo) {
			nutriologoId = usuarioQueAgenda.getUserId();
		} else {
			if (request.getNutriologoId() == null || request.getNutriologoId().isEmpty()) {
				throw new IllegalArgumentException("Debe especificarse el ID del nutriólogo para agendar la cita.");
			}
			nutriologoId = request.getNutriologoId();
			if (!obtenerNutriologo(nutriologoId)) {
				log.info("No se encontró el nutriólogo");
				throw new UsernameNotFoundException("Nutriólogo no encontrado");
			}
		}

		User nutriologo = userRepository.findByUserId(nutriologoId)
				.orElseThrow(() -> new UsernameNotFoundException("Nutriólogo no encontrado"));

		User paciente = userRepository.findByUserId(request.getPacienteId())
				.orElseThrow(() -> new UsernameNotFoundException("Paciente no encontrado"));

		boolean existeCita = citaRepository.existsByNutriologoIdAndFechaHora(nutriologo.getUserId(),
				request.getFechaHora());

		if (paciente.getUserId().equals(nutriologo.getUserId())) {
			throw new IllegalStateException("Error al agendar la cita paciente y nutriólogo son la misma persona.");
		}

		if (existeCita) {
			throw new IllegalStateException("Ya existe una cita programada para ese día y hora con el nutriólogo.");
		}

		// Crear y guardar la cita
		Cita cita = Cita.builder().pacienteId(paciente.getUserId()).nutriologoId(nutriologo.getUserId())
				.fechaHora(request.getFechaHora()).notificarPorCorreo(request.isNotificarPorCorreo())
				.agendadaPor(usuarioQueAgenda.getUserId()) // nuevo campo opcional para registrar quién la agendó
				.build();

		Cita citaGuardada = citaRepository.save(cita);

		// Enviar notificación solo si se solicitó
		if (request.isNotificarPorCorreo() && paciente.getEmail() != null) {
			emailService.enviarNotificacionCita(paciente.getEmail(), citaGuardada);
		}

		return citaGuardada;
	}

	public List<Cita> obtenerCitasUsuarioAutenticado(ConsultaCitaRequest request) {
	    String pacienteId = request.getPacienteId();
	    String nutriologoId = request.getNutriologoId();

	    // Ambos presentes: buscar por nutriólogo y paciente
	    if (pacienteId != null && !pacienteId.isEmpty() &&
	        nutriologoId != null && !nutriologoId.isEmpty()) {
	        return citaRepository.findByPacienteIdAndNutriologoId(pacienteId, nutriologoId);
	    }

	    // Solo nutriólogo
	    if (nutriologoId != null && !nutriologoId.isEmpty()) {
	        return citaRepository.findByNutriologoId(nutriologoId);
	    }

	    // Solo paciente
	    if (pacienteId != null && !pacienteId.isEmpty()) {
	        return citaRepository.findByPacienteId(pacienteId);
	    }

	    // Ninguno presente: acceso inválido
	    throw new IllegalArgumentException("Se debe proporcionar al menos pacienteId o nutriologoId");
	}


	public List<Cita> obtenerCitasPorPaciente(String userId) {
		return citaRepository.findByPacienteId(userId);
	}

	public List<Cita> obtenerCitasPorNutriologo(String nutriologoId) {
		return citaRepository.findByNutriologoId(nutriologoId);
	}

	public void verificarPermiso(User user, Permission permiso) {
		Set<Permission> permisos = user.getRoles().stream()
				.flatMap(rol -> RolePermissionMapping.getPermissionsForRole(rol).stream()).collect(Collectors.toSet());

		if (!permisos.contains(permiso)) {
			throw new AccessDeniedException("No tienes permiso para esta acción.");
		}
	}

	public boolean obtenerNutriologo(String nutriologoId) {
		Optional<User> nutriologo = userRepository.findByUserId(nutriologoId);
		if (nutriologo.isPresent()) {
			return true;
		}

		return false;
	}
}
