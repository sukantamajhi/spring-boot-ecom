package com.sukanta.springbootecom.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sukanta.springbootecom.config.ApiResponse;
import com.sukanta.springbootecom.config.Constant;
import com.sukanta.springbootecom.config.JwtAuthService;
import com.sukanta.springbootecom.model.Coupon;
import com.sukanta.springbootecom.model.enums.Role;
import com.sukanta.springbootecom.service.couponService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin
@RequestMapping("/api/coupons")
@Tag(name = "Coupons", description = "Coupons API")
@Slf4j
public class couponController {
    private final JwtAuthService jwtAuthService;
    private final couponService couponService;

    public couponController(JwtAuthService jwtAuthService, couponService couponService) {
        this.jwtAuthService = jwtAuthService;
        this.couponService = couponService;
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse<Coupon>> createCoupon(
            @RequestHeader(name = "Authorization") String token, @RequestBody Coupon request) {
        ApiResponse<Coupon> apiResponse = new ApiResponse<>();

        try {
            boolean tokenExpired = jwtAuthService.verifyJWT(token);

            if (tokenExpired) {
                apiResponse.setError(true);
                apiResponse.setCode("SESSION_EXPIRED");
                apiResponse.setMessage(Constant.SESSION_EXPIRED);

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiResponse);
            } else {
                Role userRole = jwtAuthService.getUser(token).getRole();

                if (userRole != Role.ADMIN) {
                    apiResponse.setError(true);
                    apiResponse.setCode("ACCESS_DENIED");
                    apiResponse.setMessage(Constant.ACCESS_DENIED);

                    return ResponseEntity.badRequest().body(apiResponse);
                }

                Coupon newCoupon = couponService.create(request);

                apiResponse.setError(false);
                apiResponse.setCode("COUPON_CREATED");
                apiResponse.setMessage(Constant.COUPON_CREATED);
                apiResponse.setData(newCoupon);

                return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
            }
        } catch (Exception err) {
            apiResponse.setError(true);
            apiResponse.setCode("COUPON_CREATE_FAILED");
            apiResponse.setMessage(Constant.COUPON_CREATE_FAILED);
            apiResponse.setErr(err);

            return ResponseEntity.internalServerError().body(apiResponse);
        }

    }

    @GetMapping("")
    public ResponseEntity<ApiResponse<List<Coupon>>> getAllCoupons(
            @RequestHeader(name = "Authorization") String token,
            @RequestParam(name = "limit", defaultValue = "10") int limit,
            @RequestParam(name = "page", defaultValue = "1") int page) {
        ApiResponse<List<Coupon>> apiResponse = new ApiResponse<>();

        try {
            boolean tokenExpired = jwtAuthService.verifyJWT(token);

            if (tokenExpired) {
                apiResponse.setError(true);
                apiResponse.setCode("SESSION_EXPIRED");
                apiResponse.setMessage(Constant.SESSION_EXPIRED);

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiResponse);
            }

            List<Coupon> coupons = couponService.getAll(page, limit);

            apiResponse.setError(false);
            apiResponse.setCode("COUPONS_FETCHED_SUCCESS");
            apiResponse.setMessage(Constant.COUPONS_FETCHED_SUCCESS);
            apiResponse.setData(coupons);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception err) {
            log.error("error in get coupons-->> " + err);
            apiResponse.setError(true);
            apiResponse.setCode("COUPONS_FETCH_FAILED");
            apiResponse.setMessage(Constant.COUPONS_FETCH_FAILED);
            apiResponse.setErr(err);

            return ResponseEntity.internalServerError().body(apiResponse);
        }
    }

    @GetMapping("/{couponId}")
    public ResponseEntity<ApiResponse<Coupon>> getCouponDetails(
            @RequestHeader(name = "Authorization") String token,
            @NonNull @PathVariable String couponId) {
        ApiResponse<Coupon> apiResponse = new ApiResponse<>();

        try {
            boolean tokenExpired = jwtAuthService.verifyJWT(token);

            if (tokenExpired) {
                apiResponse.setError(true);
                apiResponse.setCode("SESSION_EXPIRED");
                apiResponse.setMessage(Constant.SESSION_EXPIRED);

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiResponse);
            }

            Coupon coupon = couponService.getCouponDetails(couponId);

            apiResponse.setError(false);
            apiResponse.setCode("COUPONS_FETCHED_SUCCESS");
            apiResponse.setMessage(Constant.COUPONS_FETCHED_SUCCESS);
            apiResponse.setData(coupon);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception err) {
            log.error("error in get coupons-->> " + err);
            apiResponse.setError(true);
            apiResponse.setCode("COUPONS_FETCH_FAILED");
            apiResponse.setMessage(Constant.COUPONS_FETCH_FAILED);
            apiResponse.setErr(err);

            return ResponseEntity.internalServerError().body(apiResponse);
        }

    }

    @PutMapping("/{couponId}")
    public ResponseEntity<ApiResponse<Coupon>> UpdateCoupon(
            @NonNull @RequestHeader(name = "Authorization") String token,
            @NonNull @PathVariable String couponId,
            @NonNull @RequestBody Coupon request) {
        ApiResponse<Coupon> apiResponse = new ApiResponse<>();
        try {
            boolean tokenExpired = jwtAuthService.verifyJWT(token);

            if (tokenExpired) {
                apiResponse.setError(true);
                apiResponse.setCode("SESSION_EXPIRED");
                apiResponse.setMessage(Constant.SESSION_EXPIRED);

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiResponse);
            }

            Role userRole = jwtAuthService.getUser(token).getRole();

            if (userRole != Role.ADMIN) {
                apiResponse.setError(true);
                apiResponse.setCode("ACCESS_DENIED");
                apiResponse.setMessage(Constant.ACCESS_DENIED);

                return ResponseEntity.badRequest().body(apiResponse);
            }
            Coupon updatedCoupon = couponService.update(couponId, request);
            if (updatedCoupon != null) {
                apiResponse.setError(false);
                apiResponse.setCode("COUPON_UPDATED");
                apiResponse.setMessage(Constant.COUPON_UPDATED);
                apiResponse.setData(updatedCoupon);

                return ResponseEntity.ok().body(apiResponse);
            } else {
                apiResponse.setError(true);
                apiResponse.setCode("COUPON_UPDATE_FAILED");
                apiResponse.setMessage(Constant.COUPON_UPDATE_FAILED);

                return ResponseEntity.badRequest().body(apiResponse);
            }
        } catch (Exception e) {
            log.error("error in update coupons-->> " + e);
            apiResponse.setError(true);
            apiResponse.setCode("COUPON_UPDATE_FAILED");
            apiResponse.setMessage(Constant.INTERNAL_SERVER_ERROR);
            apiResponse.setErr(e);

            return ResponseEntity.internalServerError().body(apiResponse);
        }

    }

    @DeleteMapping("/{couponId}")
    public ResponseEntity<ApiResponse<Coupon>> UpdateCoupon(
            @NonNull @RequestHeader(name = "Authorization") String token,
            @NonNull @PathVariable String couponId) {
        ApiResponse<Coupon> apiResponse = new ApiResponse<>();
        try {
            boolean tokenExpired = jwtAuthService.verifyJWT(token);

            if (tokenExpired) {
                apiResponse.setError(true);
                apiResponse.setCode("SESSION_EXPIRED");
                apiResponse.setMessage(Constant.SESSION_EXPIRED);

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiResponse);
            }

            Role userRole = jwtAuthService.getUser(token).getRole();

            if (userRole != Role.ADMIN) {
                apiResponse.setError(true);
                apiResponse.setCode("ACCESS_DENIED");
                apiResponse.setMessage(Constant.ACCESS_DENIED);

                return ResponseEntity.badRequest().body(apiResponse);
            }
            couponService.delete(couponId);
            apiResponse.setError(false);
            apiResponse.setCode("COUPON_DELETED");
            apiResponse.setMessage(Constant.COUPON_DELETED);

            return ResponseEntity.ok().body(apiResponse);
        } catch (Exception e) {
            log.error("error in update coupons-->> " + e);
            apiResponse.setError(true);
            apiResponse.setCode("COUPON_UPDATE_FAILED");
            apiResponse.setMessage(Constant.INTERNAL_SERVER_ERROR);
            apiResponse.setErr(e);

            return ResponseEntity.internalServerError().body(apiResponse);
        }

    }

}
