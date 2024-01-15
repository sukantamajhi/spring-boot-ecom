package com.sukanta.springbootecom.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.sukanta.springbootecom.model.Coupon;

@Repository
public interface couponRepository extends MongoRepository<Coupon, String> {

}
