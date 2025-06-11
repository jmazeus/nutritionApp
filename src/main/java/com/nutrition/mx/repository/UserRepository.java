package com.nutrition.mx.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.nutrition.mx.model.User;

public interface UserRepository extends MongoRepository<User, String> {
	Optional<User> findByUsername(String username);
	
	Optional<User> findByEmail(String email);
	
	Optional<User> findByEmailAndClinicId(String email, String clinicId);
	Optional<User> findByUserId(String userId);
	List<User> findByUserIdIn(Collection<String> userIds);
	Optional<User> findByNutriologoInfo_CedulaProfesional(String cedulaProfesional);
}
