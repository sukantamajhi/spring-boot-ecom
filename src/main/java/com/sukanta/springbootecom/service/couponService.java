package com.sukanta.springbootecom.service;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;

import com.sukanta.springbootecom.model.Coupon;
import com.sukanta.springbootecom.repository.couponRepository;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class couponService {
    private final couponRepository couponRepository;

    public couponService(couponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    public Coupon create(Coupon request) throws Exception {
        try {
            Coupon coupon = Coupon.builder().amount(request.getAmount()).productId(request.getProductId()).build();

            if (coupon != null) {
                return couponRepository.save(coupon);
            } else {
                throw new Exception("Coupon is null");
            }
        } catch (Exception e) {
            log.error("Error in coupon create" + e);

            throw new Exception(e);
        }
    }

    public List<Coupon> getAll(int page, int limit) throws Exception {
        try {
            PageRequest pageRequest = PageRequest.of(page - 1, limit);
            return couponRepository.findAll(pageRequest).getContent();
        } catch (Exception e) {
            log.error("Error in coupon create" + e);

            throw new Exception(e);
        }
    }

    public Coupon getCouponDetails(@NonNull String couponId) throws Exception {
        try {
            return couponRepository.findById(couponId).orElse(null);
        } catch (Exception err) {
            log.error("Error in getting coupon details==>> " + err);

            throw new Exception(err);
        }
    }
}
