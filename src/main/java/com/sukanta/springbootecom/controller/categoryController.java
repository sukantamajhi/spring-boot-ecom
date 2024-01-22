package com.sukanta.springbootecom.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sukanta.springbootecom.config.ApiResponse;
import com.sukanta.springbootecom.config.Constant;
import com.sukanta.springbootecom.config.JwtAuthService;
import com.sukanta.springbootecom.model.Category;
import com.sukanta.springbootecom.service.categoryService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Category", description = "Category API")
@RestController
@RequestMapping("/api/categories")
@Slf4j
@CrossOrigin(origins = "http://localhost:3000, http://localhost:3001")
public class categoryController {
    private final JwtAuthService jwtAuthService;
    private final categoryService categoryService;

    public categoryController(JwtAuthService jwtAuthService, categoryService categoryService) {
        this.jwtAuthService = jwtAuthService;
        this.categoryService = categoryService;
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse<Category>> createCategory(
            @RequestHeader(name = "Authorization") String token, @RequestBody Category request) {
        ApiResponse<Category> apiResponse = new ApiResponse<>();

        try {
            boolean tokenExpired = jwtAuthService.verifyJWT(token);

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

            return ResponseEntity.status(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED)
                    .body(apiResponse);
        }
    }

    @GetMapping("")
    public ResponseEntity<ApiResponse<List<Category>>> allCategories(
            @RequestHeader(name = "Authorization") String token) {
        ApiResponse<List<Category>> apiResponse = new ApiResponse<>();
        try {
            boolean tokenExpired = jwtAuthService.verifyJWT(token);

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

            return ResponseEntity.status(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED)
                    .body(apiResponse);
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<List<Category>>> getCategoriesByUserId(
            @NonNull @RequestHeader(name = "Authorization") String token,
            @PathVariable String userId) {
        ApiResponse<List<Category>> apiResponse = new ApiResponse<>();
        try {
            boolean tokenExpired = jwtAuthService.verifyJWT(token);

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

            return ResponseEntity.status(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED)
                    .body(apiResponse);
        }
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<Category>> updateCategory(
            @NonNull @RequestHeader(name = "Authorization") String token,
            @NonNull @PathVariable String categoryId, Category request) {
        ApiResponse<Category> apiResponse = new ApiResponse<>();
        try {
            boolean tokenExpired = jwtAuthService.verifyJWT(token);

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
            log.error("Error in update category for user ==>> " + err);
            apiResponse.setError(true);
            apiResponse.setCode("PRODUCT_NOT_FOUND");
            apiResponse.setMessage(err.getMessage());

            return ResponseEntity.status(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED)
                    .body(apiResponse);
        }
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<Category>> delete(
            @NonNull @RequestHeader(name = "Authorization") String token,
            @NonNull @PathVariable String categoryId) {
        ApiResponse<Category> apiResponse = new ApiResponse<>();
        try {
            boolean tokenExpired = jwtAuthService.verifyJWT(token);

            if (tokenExpired) {
                apiResponse.setError(true);
                apiResponse.setCode("SESSION_EXPIRED");
                apiResponse.setMessage(Constant.SESSION_EXPIRED);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiResponse);
            } else {
                var userId = jwtAuthService.getUserId(token);
                categoryService.delete(categoryId, userId);

                apiResponse.setError(false);
                apiResponse.setCode("CATEGORY_DELETED");
                apiResponse.setMessage(Constant.CATEGORY_DELETED);

                return ResponseEntity.ok().body(apiResponse);
            }
        } catch (Exception err) {
            log.error("Error in delete category ==>> " + err);
            apiResponse.setError(true);
            apiResponse.setCode("PRODUCT_NOT_FOUND");
            apiResponse.setMessage(err.getMessage());

            return ResponseEntity.status(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED)
                    .body(apiResponse);
        }
    }
}
