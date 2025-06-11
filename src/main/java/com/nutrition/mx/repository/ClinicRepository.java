package com.nutrition.mx.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.nutrition.mx.model.Clinic;

public interface ClinicRepository extends MongoRepository<Clinic, String> {

}
