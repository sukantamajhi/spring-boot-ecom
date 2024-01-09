package com.sukanta.springbootecom.controller;

import com.sukanta.springbootecom.config.ApiResponse;
import com.sukanta.springbootecom.config.Constant;
import com.sukanta.springbootecom.config.LoggerUtil;
import com.sukanta.springbootecom.model.User;
import com.sukanta.springbootecom.service.userService;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class userController {

    private final userService userService;
    private final Logger log = LoggerUtil.getLogger(this);

    public userController(userService userService) {
        this.userService = userService;
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
    public ResponseEntity<ApiResponse<String>> login(@RequestBody User request) {
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

}
