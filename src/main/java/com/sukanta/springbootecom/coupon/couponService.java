package com.sukanta.springbootecom.coupon;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@Slf4j
public class couponService {
    private final couponRepository couponRepository;

    public couponService(com.sukanta.springbootecom.coupon.couponRepository couponRepository) {this.couponRepository = couponRepository;}

    public Coupon create(Coupon request) throws Exception {
        try {
            Coupon coupon = Coupon.builder().amount(request.getAmount()).productId(request.getProductId()).build();

            return couponRepository.save(coupon);
        } catch (Exception e) {
            log.error("Error in coupon create" + e);

            throw new Exception(e);
        }
    }

    public List<Coupon> getAll() throws Exception {
        try {
            return couponRepository.findAll();
        } catch (Exception e) {
            log.error("Error in coupon create" + e);

            throw new Exception(e);
        }
    }
}
