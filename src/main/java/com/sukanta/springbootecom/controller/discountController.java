package com.sukanta.springbootecom.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import com.sukanta.springbootecom.model.Discount;
import com.sukanta.springbootecom.model.enums.Role;
import com.sukanta.springbootecom.model.user.User;
import com.sukanta.springbootecom.service.discountService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;


@RestController
@Tag(name = "Discount", description = "Discount API")
@RequestMapping("/api/discounts")
@CrossOrigin
@Slf4j
public class discountController {

	private final JwtAuthService jwtAuthService;
	private final discountService discountService;

	public discountController(JwtAuthService jwtAuthService, discountService discountService) {
		this.jwtAuthService = jwtAuthService;
		this.discountService = discountService;
	}

	@PostMapping("")
	public ResponseEntity<ApiResponse<Discount>> createDiscount(
			@NonNull @RequestHeader(name = "Authorization") String token,
			@NonNull @RequestBody Discount request) {
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

	@GetMapping("")
	public ResponseEntity<ApiResponse<List<Discount>>> getDiscountsByUserId(
			@RequestHeader(name = "Authorization") String token, @RequestParam String userId) {
		ApiResponse<List<Discount>> apiResponse = new ApiResponse<>();

		try {
			boolean tokenExpired = jwtAuthService.verifyJWT(token);

			if (tokenExpired) {
				apiResponse.setError(true);
				apiResponse.setCode("UNAUTHORIZED");
				apiResponse.setMessage(Constant.UNAUTHORIZED);

				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiResponse);
			} else {
				log.info("Getting discounts by userId");
				User userDetails = jwtAuthService.getUser(token);
				if (userDetails != null) {
					List<Discount> discounts = discountService.getDiscounts();

					apiResponse.setError(false);
					apiResponse.setCode("DISCOUNTS_FOUND");
					apiResponse.setMessage(Constant.DISCOUNTS_FOUND);
					apiResponse.setData(discounts);

					return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
				} else {
					apiResponse.setError(true);
					apiResponse.setCode("ACCESS_DENIED");
					apiResponse.setMessage(Constant.ACCESS_DENIED);
					return ResponseEntity.badRequest().body(apiResponse);
				}
			}
		} catch (Exception e) {
			log.error("Error in get discounts", e);
			apiResponse.setError(true);
			apiResponse.setCode("INTERNAL_SERVER_ERROR");
			apiResponse.setErr(e);

			return ResponseEntity.internalServerError().body(apiResponse);
		}
	}

	@GetMapping("{discountId}")
	public ResponseEntity<ApiResponse<Discount>> getDiscountDetails(
			@NonNull @RequestParam String discountId) {
		ApiResponse<Discount> apiResponse = new ApiResponse<>();

		try {
			Discount discount = discountService.getDiscount(discountId);

			if (discount == null) {
				apiResponse.setError(true);
				apiResponse.setCode("DISCOUNT_NOT_FOUND");
				apiResponse.setMessage(Constant.DISCOUNT_NOT_FOUND);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
			} else {
				apiResponse.setError(false);
				apiResponse.setCode("DISCOUNT_FOUND");
				apiResponse.setMessage(Constant.DISCOUNT_FOUND);
				apiResponse.setData(discount);

				return ResponseEntity.ok().body(apiResponse);
			}

		} catch (Exception err) {
			log.error("Error in getting discount details", err);
			apiResponse.setError(true);
			apiResponse.setCode("INTERNAL_SERVER_ERROR");
			apiResponse.setMessage(Constant.INTERNAL_SERVER_ERROR);
			apiResponse.setErr(err);

			return ResponseEntity.internalServerError().body(apiResponse);
		}
	}

	@PutMapping("/{discountId}")
	public ResponseEntity<ApiResponse<Discount>> putMethodName(
			@RequestHeader(name = "Authorization") String token,
			@NonNull @PathVariable String discountId, @RequestBody Discount request) {

		ApiResponse<Discount> apiResponse = new ApiResponse<>();

		try {
			boolean tokenExpired = jwtAuthService.verifyJWT(token);

			if (tokenExpired) {
				apiResponse.setError(true);
				apiResponse.setCode("UNAUTHORIZED");
				apiResponse.setMessage(Constant.UNAUTHORIZED);

				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiResponse);
			} else {
				User userDetails = jwtAuthService.getUser(token);
				if (userDetails != null && userDetails.getRole() == Role.ADMIN) {
					Discount discount = discountService.updateDiscount(request, discountId);
					if (discount == null) {
						apiResponse.setError(true);
						apiResponse.setCode("DISCOUNT_NOT_FOUND");
						apiResponse.setMessage(Constant.DISCOUNT_NOT_FOUND);
						return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
					} else {
						apiResponse.setError(false);
						apiResponse.setCode("DISCOUNT_UPDATED");
						apiResponse.setMessage(Constant.DISCOUNT_UPDATED);
						apiResponse.setData(discount);
						return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
					}
				} else {
					apiResponse.setError(true);
					apiResponse.setCode("ACCESS_DENIED");
					apiResponse.setMessage(Constant.ACCESS_DENIED);
					return ResponseEntity.badRequest().body(apiResponse);
				}
			}

		} catch (Exception err) {
			log.error("Error in verifying token", err);
			apiResponse.setError(true);
			apiResponse.setCode("INTERNAL_SERVER_ERROR");
			apiResponse.setMessage(Constant.INTERNAL_SERVER_ERROR);
			apiResponse.setErr(err);

			return ResponseEntity.internalServerError().body(apiResponse);
		}
	}
}
