package com.sukanta.springbootecom.repository;

import com.sukanta.springbootecom.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface productRepository extends MongoRepository<Product, String> {
    List<Product> getProductsByCreatedByOrderByCreatedAtDesc(String createdBy);

    List<Product> findByCreatedByAndDescriptionContainingIgnoreCaseOrNameContainingIgnoreCaseOrderByCreatedAtDesc(String createdBy, String name, String description);

    List<Product> findByDescriptionContainingIgnoreCaseOrNameContainingIgnoreCaseOrderByCreatedAtDesc(String name, String description);

    Product findByCreatedByAndId(String createdBy, String id);

}
