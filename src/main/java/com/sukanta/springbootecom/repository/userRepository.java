package com.sukanta.springbootecom.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.sukanta.springbootecom.model.user.User;

@Repository
public interface userRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
}
