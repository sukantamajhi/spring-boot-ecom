package com.sukanta.springbootecom.controller;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
import com.sukanta.springbootecom.model.enums.Role;
import com.sukanta.springbootecom.model.user.LoginUser;
import com.sukanta.springbootecom.model.user.User;
import com.sukanta.springbootecom.service.userService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "User", description = "User controller")
@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class userController {

    private final userService userService;
    private final Logger log = LoggerUtil.getLogger(this);
    private final JwtAuthService jwtAuthService;

    public userController(userService userService, JwtAuthService jwtAuthService) {
        this.userService = userService;
        this.jwtAuthService = jwtAuthService;
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse<User>> register(@RequestBody User request) {
        ApiResponse<User> apiResponse = new ApiResponse<>();
        try {
            User newUser = userService.registerUser(request);

            apiResponse.setError(false);
            apiResponse.setCode("USER_CREATED");
            apiResponse.setMessage(Constant.USER_CREATED);
            apiResponse.setData(newUser);

            return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
        } catch (Exception err) {
            apiResponse.setError(false);
            apiResponse.setCode("USER_CREATE_FAILED");
            apiResponse.setMessage(err.getLocalizedMessage());
            apiResponse.setErr(err);

            return ResponseEntity.internalServerError().body(apiResponse);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody LoginUser request) {
        ApiResponse<String> apiResponse = new ApiResponse<>();

        try {
            String access_token = userService.login(request);
            apiResponse.setError(false);
            apiResponse.setAccess_token(access_token);
            apiResponse.setMessage(Constant.LOGIN_SUCCESS);
            apiResponse.setCode("LOGIN_SUCCESS");

            return ResponseEntity.ok().body(apiResponse);
        } catch (Exception e) {
            log.error("Error in login api ==>>", e);
            apiResponse.setError(true);
            apiResponse.setMessage(e.getLocalizedMessage());
            apiResponse.setCode("LOGIN_FAILED");
            apiResponse.setErr(e);

            return ResponseEntity.internalServerError().body(apiResponse);
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<User>> updateUser(
            @RequestHeader(name = "Authorization") String token, @RequestBody User request,
            @PathVariable String userId) {
        var apiResponse = new ApiResponse<User>();

        try {
            boolean tokenExpired = jwtAuthService.verifyJWT(token);

            if (tokenExpired) {
                apiResponse.setError(true);
                apiResponse.setCode("SESSION_EXPIRED");
                apiResponse.setMessage(Constant.SESSION_EXPIRED);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiResponse);
            } else {
                var userDetails = jwtAuthService.getUser(token);

                if (userDetails.getRole() == Role.ADMIN) {
                    var updatedUser = userService.update(userId, request);
                    apiResponse.setError(false);
                    apiResponse.setCode("USER_UPDATED");
                    apiResponse.setMessage(Constant.USER_UPDATED);
                    apiResponse.setData(updatedUser);
                    return ResponseEntity.ok().body(apiResponse);
                } else {
                    apiResponse.setError(true);
                    apiResponse.setCode("ACCESS_DENIED");
                    apiResponse.setMessage(Constant.ACCESS_DENIED);

                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiResponse);
                }
            }
        } catch (Exception err) {
            log.error("Error in user update ==>> " + err);
            apiResponse.setError(true);
            apiResponse.setCode("INTERNAL_SERVER_ERROR");
            apiResponse.setMessage(err.getMessage());
            apiResponse.setErr(err);
            return ResponseEntity.internalServerError().body(apiResponse);
        }
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<ApiResponse<User>> changeStatus(
            @NotNull @RequestHeader(name = "Authorization") String token, @PathVariable String userId) {
        var apiResponse = new ApiResponse<User>();

        try {
            boolean tokenExpired = jwtAuthService.verifyJWT(token);

            if (tokenExpired) {
                apiResponse.setError(true);
                apiResponse.setCode("SESSION_EXPIRED");
                apiResponse.setMessage(Constant.SESSION_EXPIRED);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiResponse);
            } else {
                var userDetails = jwtAuthService.getUser(token);

                if (userDetails.getRole() == Role.ADMIN) {
                    var updatedUser = userService.changeStatus(userId);
                    apiResponse.setError(false);
                    apiResponse.setCode("USER_UPDATED");
                    apiResponse.setMessage(Constant.USER_UPDATED);
                    apiResponse.setData(updatedUser);
                    return ResponseEntity.ok().body(apiResponse);
                } else {
                    apiResponse.setError(true);
                    apiResponse.setCode("ACCESS_DENIED");
                    apiResponse.setMessage(Constant.ACCESS_DENIED);

                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiResponse);
                }
            }
        } catch (Exception e) {
            log.error("Error in user status change ==>> ", e);
            apiResponse.setError(true);
            apiResponse.setCode("INTERNAL_SERVER_ERROR");
            apiResponse.setMessage(e.getMessage());
            apiResponse.setErr(e);
            return ResponseEntity.internalServerError().body(apiResponse);
        }
    }

    @GetMapping("/")
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers(
            @RequestHeader(name = "Authorization") String token,
            @RequestParam(name = "page", defaultValue = "1") int page) throws Exception {
        var apiResponse = new ApiResponse<List<User>>();
        try {
            boolean tokenExpired = jwtAuthService.verifyJWT(token);
            if (tokenExpired) {
                ApiResponse.builder().error(true).code("TOKEN_EXPIRED").message(Constant.SESSION_EXPIRED).build();
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiResponse);
            } else {
                var userDetails = jwtAuthService.getUser(token);

                if (userDetails.getRole() == Role.ADMIN) {
                    List<User> users = userService.getAllUsers(page);

                    apiResponse.setError(false);
                    apiResponse.setCode("USERS_FETCHED");
                    apiResponse.setMessage(Constant.USERS_FETCHED_SUCCESS);
                    apiResponse.setData(users);

                    return ResponseEntity.ok().body(apiResponse);
                } else {
                    apiResponse.setError(true);
                    apiResponse.setCode("ACCESS_DENIED");
                    apiResponse.setMessage(Constant.ACCESS_DENIED);

                    return ResponseEntity.badRequest().body(apiResponse);
                }
            }
        } catch (Exception e) {
            log.error("Error in getting all users", e);

            apiResponse.setError(true);
            apiResponse.setCode("INTERNAL_SERVER_ERROR");
            apiResponse.setMessage(e.getMessage());
            apiResponse.setErr(e);

            return ResponseEntity.internalServerError().body(apiResponse);
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<User>> getUserById(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable String userId) throws Exception {
        var apiResponse = new ApiResponse<User>();
        try {
            boolean tokenExpired = jwtAuthService.verifyJWT(token);

            if (tokenExpired) {
                apiResponse.setError(true);
                apiResponse.setCode("SESSION_EXPIRED");
                apiResponse.setMessage(Constant.SESSION_EXPIRED);

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiResponse);
            } else {
                var user = jwtAuthService.getUser(token);

                if (user.getRole() == Role.ADMIN) {
                    User userDetails = userService.getUserById(userId);

                    apiResponse.setError(false);
                    apiResponse.setCode("USERS_FETCHED");
                    apiResponse.setMessage(Constant.USERS_FETCHED_SUCCESS);
                    apiResponse.setData(userDetails);

                    return ResponseEntity.ok().body(apiResponse);
                } else {
                    apiResponse.setError(true);
                    apiResponse.setCode("ACCESS_DENIED");
                    apiResponse.setMessage(Constant.ACCESS_DENIED);

                    return ResponseEntity.badRequest().body(apiResponse);
                }
            }
        } catch (Exception e) {
            log.error("Error in getting user details ==>> ", e);
            apiResponse.setError(true);
            apiResponse.setCode("INTERNAL_SERVER_ERROR");
            apiResponse.setMessage(e.getMessage());
            apiResponse.setErr(e);

            return ResponseEntity.internalServerError().body(apiResponse);

        }
    }
}