package com.nutrition.mx.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.nutrition.mx.model.Cita;

public interface CitaRepository extends MongoRepository<Cita, String> {
	List<Cita> findByPacienteId(String pacienteId);

	List<Cita> findByNutriologoId(String nutriologoId);

	boolean existsByNutriologoIdAndFechaHora(String nutriologoId, LocalDateTime fechaHora);

	List<Cita> findByPacienteIdAndNutriologoId(String pacienteId, String nutriologoId);

//	List<Cita> findByPacienteIdAndNutriologoIdAndFechaHoraBetween(String pacienteId, String nutriologoId,
//			LocalDateTime desde, LocalDateTime hasta);

//	List<Cita> findByNutriologoIdAndFechaHoraBetween(String nutriologoId, LocalDateTime desde, LocalDateTime hasta);
//	List<Cita> findByPacienteIdAndFechaHoraBetween(String pacienteId, LocalDateTime desde, LocalDateTime hasta);
}
