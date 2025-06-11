package com.nutrition.mx.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.nutrition.mx.model.Role;

public interface RoleRepository extends MongoRepository<Role, String> {
	List<Role> findByClinicId(String clinicId);
	
	List<Role> findByNameInAndClinicId(String[] names, String clinicId);

    // Alternativa: permitir también roles globales (clinicId == null)
    @Query("{ 'name': { $in: ?0 }, $or: [ { 'clinicId': ?1 }, { 'clinicId': null } ] }")
    List<Role> findByNameInAndClinicIdOrGlobal(Set<String> names, String clinicId);

}
