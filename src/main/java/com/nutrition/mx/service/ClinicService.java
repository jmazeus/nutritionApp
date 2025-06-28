package com.nutrition.mx.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.nutrition.mx.dto.ClinicDto;
import com.nutrition.mx.enums.Permission;
import com.nutrition.mx.enums.RoleName;
import com.nutrition.mx.model.Clinic;
import com.nutrition.mx.model.User;
import com.nutrition.mx.repository.ClinicRepository;
import com.nutrition.mx.repository.UserRepository;
import com.nutrition.mx.response.ClinicResponse;
import com.nutrition.mx.security.PermissionChecker;
import com.nutrition.mx.utils.SequenceGeneratorService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.data.mongodb.core.query.Criteria;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClinicService {

	private final ClinicRepository clinicRepository;
	private final SequenceGeneratorService sequenceGeneratorService;
	private final MongoTemplate mongoTemplate;
	private final UserRepository userRepository;

	public Clinic crearClinica(Clinic clinic, User user) {
		log.info("Clinica: {}", clinic);
		log.info("User: {}", user);
		if (!user.getRoles().contains(RoleName.SUPER_ADMIN)) {
			throw new AccessDeniedException("No tienes permisos para crear clínicas");
		}

		String newClinicId = sequenceGeneratorService.generateSequence("clinic_sequence");
		clinic.setCreatedBy(user.getUserId());
		clinic.setClinicId(newClinicId);
		clinic.setCreatedAt(new Date());
		clinic.setCreatedBy(user.getUserId());

		// Aquí podrías agregar más validaciones si lo deseas (nombre único, etc.)
		return clinicRepository.save(clinic);
	}

	public ClinicResponse findClinics(String name, Date dateInit, Date dateEnd, String createdBy, int page, int size,
			String sortBy, String direction, User usuarioAutenticado) {

		User currentUser = userRepository.findByUsername(usuarioAutenticado.getUsername())
				.orElseThrow(() -> new UsernameNotFoundException("Usuario actual no encontrado"));
		PermissionChecker.verificarPermiso(currentUser, Permission.CLINICA_VER);

		List<Criteria> criteriaList = new ArrayList<>();

		if (name != null && !name.isEmpty()) {
			criteriaList.add(Criteria.where("name").regex(name, "i"));
		}

		if (dateInit != null && dateEnd != null) {
			criteriaList.add(Criteria.where("createdAt").gte(dateInit).lte(dateEnd));
		} else if (dateInit != null) {
			criteriaList.add(Criteria.where("createdAt").gte(dateInit));
		} else if (dateEnd != null) {
			criteriaList.add(Criteria.where("createdAt").lte(dateEnd));
		}

		if (createdBy != null && !createdBy.isEmpty()) {
			criteriaList.add(Criteria.where("createdBy").is(createdBy));
		}

		Criteria finalCriteria = new Criteria();
		if (!criteriaList.isEmpty()) {
			finalCriteria.andOperator(criteriaList.toArray(new Criteria[0]));
		}

		Query query = new Query(finalCriteria);

		long total = mongoTemplate.count(query, Clinic.class);

		// Paginación y ordenamiento
		Sort.Direction sortDirection = Sort.Direction.fromString(direction);
		Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
		query.with(pageable);

		List<Clinic> clinics = mongoTemplate.find(query, Clinic.class);

		// Obtener los IDs únicos de createdBy
		Set<String> userIds = clinics.stream().map(Clinic::getCreatedBy).filter(Objects::nonNull)
				.collect(Collectors.toSet());

		// Consultar los nombres de los usuarios que crearon las clínicas
		List<User> users = userRepository.findByUserIdIn(userIds);
		Map<String, String> userIdToNameMap = users.stream()
				.collect(Collectors.toMap(User::getUserId, User::getFullName));

		// Transformar clínicas a DTO con nombre del creador
		List<ClinicDto> dtos = clinics.stream()
				.map(clinic -> ClinicDto.builder().id(clinic.getId()).name(clinic.getName())
						.address(clinic.getAddress()).createdBy(clinic.getCreatedBy())
						.createdByName(userIdToNameMap.getOrDefault(clinic.getCreatedBy(), "Desconocido"))
						.clinicId(clinic.getClinicId()).telephone(clinic.getTelephone()).email(clinic.getEmail())
						.url(clinic.getUrl()).createdAt(clinic.getCreatedAt()).build())
				.collect(Collectors.toList());

		int totalPages = (int) Math.ceil((double) total / size);
		boolean last = (page + 1) * size >= total;

		return new ClinicResponse(dtos, page, size, total, totalPages, last);
	}

	public Clinic actualizarClinica(String clinicId, Clinic clinicUpdate, User user) {
		if (!user.getRoles().contains(RoleName.SUPER_ADMIN)) {
			throw new AccessDeniedException("No tienes permisos para actualizar clínicas");
		}

		Clinic existingClinic = clinicRepository.findByClinicId(clinicId)
				.orElseThrow(() -> new IllegalArgumentException("Clínica no encontrada"));

		// Actualiza solo los campos necesarios, aquí un ejemplo:
		existingClinic.setName(clinicUpdate.getName());
		existingClinic.setAddress(clinicUpdate.getAddress());
		existingClinic.setTelephone(clinicUpdate.getTelephone());
		existingClinic.setEmail(clinicUpdate.getEmail());
		existingClinic.setUrl(clinicUpdate.getUrl());
		// Puedes agregar más campos según lo que permita tu modelo

		return clinicRepository.save(existingClinic);
	}
}
