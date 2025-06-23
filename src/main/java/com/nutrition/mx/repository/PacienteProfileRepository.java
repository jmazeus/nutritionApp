package com.nutrition.mx.repository;

import com.nutrition.mx.model.PacienteProfile;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PacienteProfileRepository extends MongoRepository<PacienteProfile, String> {

    List<PacienteProfile> findByNutriologosIdsContaining(String nutriologoId);

    List<PacienteProfile> findByNutriologoActualId(String nutriologoId);
}
