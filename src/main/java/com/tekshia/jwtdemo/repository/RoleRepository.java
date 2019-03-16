package com.tekshia.jwtdemo.repository;

import com.tekshia.jwtdemo.entity.Role;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends MongoRepository<Role, String> {
    Role findByName(String name);
}
