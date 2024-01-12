package com.sukanta.springbootecom.coupon;

import com.sukanta.springbootecom.config.ApiResponse;
import com.sukanta.springbootecom.config.Constant;
import com.sukanta.springbootecom.config.JwtAuthService;
import com.sukanta.springbootecom.model.enums.Role;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
            @RequestHeader(name = "Authorization") String token, @RequestBody Coupon request
    ) {
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
            @RequestHeader(name = "Authorization") String token
    ) {
        ApiResponse<List<Coupon>> apiResponse = new ApiResponse<>();

        try {
            boolean tokenExpired = jwtAuthService.verifyJWT(token);

            if (tokenExpired) {
                apiResponse.setError(true);
                apiResponse.setCode("SESSION_EXPIRED");
                apiResponse.setMessage(Constant.SESSION_EXPIRED);

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiResponse);
            }

            List<Coupon> coupons = couponService.getAll();

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

}