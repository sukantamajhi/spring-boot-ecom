package com.sukanta.springbootecom.controller;

import java.util.List;

import org.slf4j.Logger;
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
import com.sukanta.springbootecom.config.LoggerUtil;
import com.sukanta.springbootecom.model.Product;
import com.sukanta.springbootecom.service.productService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Product", description = "Product API")
@RestController
@RequestMapping("/api/products")
@CrossOrigin
public class productController {

    private final productService productService;
    private final JwtAuthService jwtAuthService;
    private final Logger log = LoggerUtil.getLogger(this);

    public productController(productService productService, JwtAuthService jwtAuthService) {
        this.productService = productService;
        this.jwtAuthService = jwtAuthService;
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse<Product>> createProduct(
            @NonNull @RequestHeader(name = "Authorization") String token, @RequestBody Product request)
            throws Exception {
        ApiResponse<Product> apiResponse = new ApiResponse<>();
        var tokenExpired = jwtAuthService.verifyJWT(token);

        if (tokenExpired) {
            apiResponse.setError(true);
            apiResponse.setCode("SESSION_EXPIRED");
            apiResponse.setMessage(Constant.SESSION_EXPIRED);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiResponse);
        } else {
            var userId = jwtAuthService.getUserId(token);
            var product = productService.createProduct(request, userId);

            apiResponse.setData(product);
            apiResponse.setError(false);
            apiResponse.setCode("PRODUCT_ADDED");
            apiResponse.setMessage(Constant.PRODUCT_ADDED);

            return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
        }
    }

    @GetMapping("")
    public ResponseEntity<ApiResponse<List<Product>>> getProductsByUserId(
            @NonNull @RequestHeader(name = "Authorization") String token, @RequestParam(name = "userId") String userId,
            @RequestParam(name = "searchString", required = false) String searchString)
            throws Exception {
        ApiResponse<List<Product>> apiResponse = new ApiResponse<>();
        var tokenExpired = jwtAuthService.verifyJWT(token);

        if (tokenExpired) {
            apiResponse.setError(true);
            apiResponse.setCode("SESSION_EXPIRED");
            apiResponse.setMessage(Constant.SESSION_EXPIRED);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiResponse);
        } else {
            List<Product> products = productService.getProducts(userId, searchString);

            apiResponse.setError(false);
            apiResponse.setCode("PRODUCTS_FETCH_SUCCESS");
            apiResponse.setMessage(Constant.PRODUCTS_FETCH_SUCCESS);
            apiResponse.setData(products);

            return ResponseEntity.ok().body(apiResponse);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<Product>>> getAllProducts(
            @NonNull @RequestHeader(name = "Authorization") String token,
            @RequestParam(name = "searchString", required = false) String searchString)
            throws Exception {
        ApiResponse<List<Product>> apiResponse = new ApiResponse<>();
        var tokenExpired = jwtAuthService.verifyJWT(token);

        if (tokenExpired) {
            apiResponse.setError(true);
            apiResponse.setCode("SESSION_EXPIRED");
            apiResponse.setMessage(Constant.SESSION_EXPIRED);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiResponse);
        } else {
            List<Product> products = productService.getAllProducts(searchString);

            apiResponse.setError(false);
            apiResponse.setCode("PRODUCTS_FETCH_SUCCESS");
            apiResponse.setMessage(Constant.PRODUCTS_FETCH_SUCCESS);
            apiResponse.setData(products);

            return ResponseEntity.ok().body(apiResponse);
        }
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ApiResponse<Product>> updateProduct(
            @NonNull @RequestHeader(name = "Authorization") String token, @PathVariable String productId,
            @RequestBody Product request) throws Exception {
        ApiResponse<Product> apiResponse = new ApiResponse<>();
        boolean tokenExpired = jwtAuthService.verifyJWT(token);

        if (tokenExpired) {
            apiResponse.setError(true);
            apiResponse.setCode("SESSION_EXPIRED");
            apiResponse.setMessage(Constant.SESSION_EXPIRED);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiResponse);
        } else {
            var userId = jwtAuthService.getUserId(token);
            try {
                Product updateProduct = productService.updateProduct(userId, productId, request);

                apiResponse.setError(false);
                apiResponse.setCode("PRODUCT_UPDATED");
                apiResponse.setMessage(Constant.PRODUCT_UPDATED);
                apiResponse.setData(updateProduct);

                return ResponseEntity.ok().body(apiResponse);
            } catch (Exception e) {
                apiResponse.setError(true);
                apiResponse.setCode("PRODUCT_NOT_FOUND");
                apiResponse.setMessage(e.getMessage());

                return ResponseEntity.internalServerError().body(apiResponse);
            }
        }
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse<Product>> deleteProduct(
            @NonNull @RequestHeader(name = "Authorization") String token, @PathVariable String productId)
            throws Exception {
        ApiResponse<Product> apiResponse = new ApiResponse<>();
        boolean tokenExpired = jwtAuthService.verifyJWT(token);

        if (tokenExpired) {
            apiResponse.setError(true);
            apiResponse.setCode("SESSION_EXPIRED");
            apiResponse.setMessage(Constant.SESSION_EXPIRED);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiResponse);
        } else {
            var userId = jwtAuthService.getUserId(token);
            try {
                productService.deleteProduct(userId, productId);

                apiResponse.setError(false);
                apiResponse.setMessage(Constant.PRODUCT_DELETED_SUCCESS);
                apiResponse.setCode("PRODUCT_DELETED_SUCCESS");
                return ResponseEntity.ok().body(apiResponse);
            } catch (Exception err) {
                log.error("Error in delete product ==>> ", err);
                apiResponse.setError(true);
                apiResponse.setCode("INTERNAL_SERVER_ERROR");
                apiResponse.setMessage(err.getMessage());

                return ResponseEntity.internalServerError().body(apiResponse);
            }
        }
    }

    @PutMapping("/status/{productId}")
    public ResponseEntity<ApiResponse<Product>> updateProductStatus(
            @NonNull @RequestHeader(name = "Authorization") String token, @PathVariable String productId)
            throws Exception {
        ApiResponse<Product> apiResponse = new ApiResponse<>();
        boolean tokenExpired = jwtAuthService.verifyJWT(token);

        if (tokenExpired) {
            apiResponse.setError(true);
            apiResponse.setCode("SESSION_EXPIRED");
            apiResponse.setMessage(Constant.SESSION_EXPIRED);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiResponse);
        } else {
            var userId = jwtAuthService.getUserId(token);

            try {
                Product updatedProduct = productService.changeStatus(userId, productId);

                apiResponse.setError(false);
                apiResponse.setCode("PRODUCT_STATUS_UPDATED");
                apiResponse.setMessage(Constant.PRODUCT_STATUS_UPDATED);
                apiResponse.setData(updatedProduct);
                return ResponseEntity.ok().body(apiResponse);

            } catch (Exception e) {
                log.error("Error in change product status", e);
                ApiResponse.builder()
                        .error(true)
                        .code("INTERNAL_SERVER_ERROR")
                        .message(e.getMessage())
                        .err(e)
                        .build();
                return ResponseEntity.internalServerError().body(apiResponse);
            }
        }
    }
}