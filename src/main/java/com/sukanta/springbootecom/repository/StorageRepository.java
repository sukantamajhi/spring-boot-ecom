package com.sukanta.springbootecom.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sukanta.springbootecom.model.File;

public interface StorageRepository extends MongoRepository<File, String> {
	File findByFileName(String name);
}
