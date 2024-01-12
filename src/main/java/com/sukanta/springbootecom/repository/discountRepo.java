package com.sukanta.springbootecom.repository;

import com.sukanta.springbootecom.model.Discount;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface discountRepo extends MongoRepository<Discount, String> {

    Optional<List<Discount>> findAllByStatusAndProductIdAndIdIsNot(boolean status, String productId, String id);

}
