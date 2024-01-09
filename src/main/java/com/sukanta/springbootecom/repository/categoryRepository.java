package com.sukanta.springbootecom.repository;

import com.sukanta.springbootecom.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface categoryRepository extends MongoRepository<Category, String> {
    List<Category> findByCreatedBy(String userId);
}
