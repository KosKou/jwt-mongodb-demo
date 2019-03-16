package com.tekshia.jwtdemo.repository;

import com.tekshia.jwtdemo.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    User findByEmail(String email);
    List<User> findAllByFirstName(String firstName);
}
