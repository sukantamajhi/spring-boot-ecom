package com.sukanta.springbootecom.coupon;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface couponRepository extends MongoRepository<Coupon, String> {

}
