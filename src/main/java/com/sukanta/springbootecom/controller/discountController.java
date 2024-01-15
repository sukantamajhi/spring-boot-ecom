package com.sukanta.springbootecom.controller;

import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sukanta.springbootecom.config.ApiResponse;
import com.sukanta.springbootecom.config.Constant;
import com.sukanta.springbootecom.config.JwtAuthService;
import com.sukanta.springbootecom.config.LoggerUtil;
import com.sukanta.springbootecom.model.Discount;
import com.sukanta.springbootecom.model.enums.Role;
import com.sukanta.springbootecom.model.user.User;
import com.sukanta.springbootecom.service.discountService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Discount", description = "Discount API")
@RequestMapping("/api/discounts")
@CrossOrigin
public class discountController {

    private final Logger log = LoggerUtil.getLogger(this);

    private final JwtAuthService jwtAuthService;
    private final discountService discountService;

    public discountController(JwtAuthService jwtAuthService, discountService discountService) {
        this.jwtAuthService = jwtAuthService;
        this.discountService = discountService;
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse<Discount>> createDiscount(
            @NonNull @RequestHeader(name = "Authorization") String token, @NonNull @RequestBody Discount request) {
        ApiResponse<Discount> apiResponse = new ApiResponse<>();
        try {
            boolean tokenExpired = jwtAuthService.verifyJWT(token);
            if (tokenExpired) {
                apiResponse.setError(true);
                apiResponse.setCode("UNAUTHORIZED");
                apiResponse.setMessage(Constant.UNAUTHORIZED);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiResponse);
            } else {
                log.info("Creating discount");
                User userDetails = jwtAuthService.getUser(token);
                if (userDetails != null && userDetails.getRole() == Role.ADMIN) {
                    Discount discount = discountService.createDiscount(request, userDetails);

                    apiResponse.setError(false);
                    apiResponse.setCode("DISCOUNT_CREATED");
                    apiResponse.setMessage(Constant.DISCOUNT_CREATED);
                    apiResponse.setData(discount);

                    return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
                } else {
                    apiResponse.setError(true);
                    apiResponse.setCode("ACCESS_DENIED");
                    apiResponse.setMessage(Constant.ACCESS_DENIED);
                    return ResponseEntity.badRequest().body(apiResponse);
                }
            }
        } catch (Exception e) {
            log.error("Error in creating discount", e);
            apiResponse.setError(true);
            apiResponse.setCode("INTERNAL_SERVER_ERROR");
            apiResponse.setErr(e);

            return ResponseEntity.internalServerError().body(apiResponse);
        }
    }

}
