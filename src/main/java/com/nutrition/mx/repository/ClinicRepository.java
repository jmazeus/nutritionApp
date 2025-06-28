package com.nutrition.mx.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.nutrition.mx.model.Clinic;

public interface ClinicRepository extends MongoRepository<Clinic, String> {
	Optional<Clinic> findByClinicId(String clinicId);

}
