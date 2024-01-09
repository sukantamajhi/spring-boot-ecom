package com.sukanta.springbootecom.controller;

import com.sukanta.springbootecom.config.ApiResponse;
import com.sukanta.springbootecom.config.Constant;
import com.sukanta.springbootecom.config.JwtAuthService;
import com.sukanta.springbootecom.config.LoggerUtil;
import com.sukanta.springbootecom.model.Category;
import com.sukanta.springbootecom.service.categoryService;
import lombok.NonNull;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class categoryController {
    private final JwtAuthService jwtAuthService;
    private final categoryService categoryService;
    private final Logger log = LoggerUtil.getLogger(this);

    public categoryController(JwtAuthService jwtAuthService, categoryService categoryService) {
        this.jwtAuthService = jwtAuthService;
        this.categoryService = categoryService;
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse<Category>> createCategory(@RequestHeader(name = "Authorization") String token, @RequestBody Category request) {
        ApiResponse<Category> apiResponse = new ApiResponse<>();

        try {
            boolean tokenExpired = jwtAuthService.verifyJWT(token, apiResponse);

            if (tokenExpired) {
                apiResponse.setError(true);
                apiResponse.setCode("SESSION_EXPIRED");
                apiResponse.setMessage(Constant.SESSION_EXPIRED);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiResponse);
            } else {
                var userId = jwtAuthService.getUserId(token);

                var result = categoryService.createCategory(request, userId);

                apiResponse.setError(false);
                apiResponse.setMessage(Constant.CATEGORY_CREATED);
                apiResponse.setCode("CATEGORY_CREATED");
                apiResponse.setData(result);

                return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
            }
        } catch (Exception e) {
            log.error("Error in create category ==>> " + e);
            apiResponse.setError(true);
            apiResponse.setCode("PRODUCT_NOT_FOUND");
            apiResponse.setMessage(e.getMessage());

            return ResponseEntity.status(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED).body(apiResponse);
        }
    }

    @GetMapping("")
    public ResponseEntity<ApiResponse<List<Category>>> allCategories(@RequestHeader(name = "Authorization") String token) {
        ApiResponse<List<Category>> apiResponse = new ApiResponse<>();
        try {
            boolean tokenExpired = jwtAuthService.verifyJWT(token, apiResponse);

            if (tokenExpired) {
                apiResponse.setError(true);
                apiResponse.setCode("SESSION_EXPIRED");
                apiResponse.setMessage(Constant.SESSION_EXPIRED);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiResponse);
            } else {
                var result = categoryService.getAllCategories();

                apiResponse.setError(false);
                apiResponse.setMessage(Constant.CATEGORIES_FETCHED);
                apiResponse.setCode("CATEGORIES_FETCHED");
                apiResponse.setData(result);

                return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
            }
        } catch (Exception err) {
            log.error("Error in get all categories ==>> " + err);
            apiResponse.setError(true);
            apiResponse.setCode("PRODUCT_NOT_FOUND");
            apiResponse.setMessage(err.getMessage());

            return ResponseEntity.status(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED).body(apiResponse);
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<List<Category>>> getCategoriesByUserId(@NonNull @RequestHeader(name = "Authorization") String token, @PathVariable String userId) {
        ApiResponse<List<Category>> apiResponse = new ApiResponse<>();
        try {
            boolean tokenExpired = jwtAuthService.verifyJWT(token, apiResponse);

            if (tokenExpired) {
                apiResponse.setError(true);
                apiResponse.setCode("SESSION_EXPIRED");
                apiResponse.setMessage(Constant.SESSION_EXPIRED);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiResponse);
            } else {
                var result = categoryService.getCategoriesByUserId(userId);

                apiResponse.setError(false);
                apiResponse.setMessage(Constant.CATEGORIES_FETCHED);
                apiResponse.setCode("CATEGORIES_FETCHED");
                apiResponse.setData(result);

                return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
            }
        } catch (Exception err) {
            log.error("Error in get categories for user ==>> " + err);
            apiResponse.setError(true);
            apiResponse.setCode("PRODUCT_NOT_FOUND");
            apiResponse.setMessage(err.getMessage());

            return ResponseEntity.status(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED).body(apiResponse);
        }
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<Category>> updateCategory(
            @NonNull @RequestHeader(name = "Authorization") String token, @PathVariable String categoryId, Category request
    ) {
        ApiResponse<Category> apiResponse = new ApiResponse<>();
        try {
            boolean tokenExpired = jwtAuthService.verifyJWT(token, apiResponse);

            if (tokenExpired) {
                apiResponse.setError(true);
                apiResponse.setCode("SESSION_EXPIRED");
                apiResponse.setMessage(Constant.SESSION_EXPIRED);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiResponse);
            } else {
                var userId = jwtAuthService.getUserId(token);

                var result = categoryService.update(categoryId, userId, request);

                apiResponse.setError(false);
                apiResponse.setMessage(Constant.CATEGORY_UPDATED);
                apiResponse.setCode("CATEGORY_UPDATED");
                apiResponse.setData(result);

                return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
            }
        } catch (Exception err) {
            log.error("Error in get categories for user ==>> " + err);
            apiResponse.setError(true);
            apiResponse.setCode("PRODUCT_NOT_FOUND");
            apiResponse.setMessage(err.getMessage());

            return ResponseEntity.status(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED).body(apiResponse);
        }
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<Category>> delete(
            @NonNull @RequestHeader(name = "Authorization") String token, @PathVariable String categoryId
    ) {
        return null;
    }
}
