package com.sukanta.springbootecom.service;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.sukanta.springbootecom.config.Constant;
import com.sukanta.springbootecom.config.JwtAuthService;
import com.sukanta.springbootecom.config.LoggerUtil;
import com.sukanta.springbootecom.model.enums.Role;
import com.sukanta.springbootecom.model.user.LoginUser;
import com.sukanta.springbootecom.model.user.User;
import com.sukanta.springbootecom.repository.userRepository;

import lombok.NonNull;

@Service
public class userService {
    private final Logger log = LoggerUtil.getLogger(this);
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
    private final userRepository userRepository;
    private final JwtAuthService jwtAuthService;

    public userService(userRepository userRepository, JwtAuthService jwtAuthService) {
        this.userRepository = userRepository;
        this.jwtAuthService = jwtAuthService;
    }

    public User registerUser(@NotNull User request) throws Exception {
        var ExistingUser = userRepository.findByEmail(request.getEmail()).orElse(null);

        if (ExistingUser == null) {
            var user = User.builder().name(request.getName()).email(request.getEmail()).phone(request.getPhone())
                    .address(request.getAddress()).role(Role.USER).password(encoder.encode(request.getPassword()))
                    .build();
            try {
                if (user != null) {
                    return userRepository.save(user);
                } else {
                    throw new Exception(Constant.USER_CREATE_FAILED);
                }
            } catch (Exception err) {
                log.error("Error in saving data to database", err);
                throw new Exception(Constant.USER_CREATE_FAILED);
            }
        } else {
            throw new Exception(Constant.USER_EXIST);
        }
    }

    public String login(@NotNull LoginUser request) throws Exception {
        var ExistingUser = userRepository.findByEmail(request.getEmail()).orElse(null);

        if (ExistingUser == null) {
            throw new Exception(Constant.USER_NOT_FOUND);
        } else {
            if (encoder.matches(request.getPassword(), ExistingUser.getPassword())) {
                return jwtAuthService.generateToken(ExistingUser.getEmail(), ExistingUser.getId());
            } else {
                throw new Exception(Constant.PASSWORD_MISMATCHED);
            }
        }
    }

    public User update(@NonNull String userId, User request) throws Exception {
        var existingUser = userRepository.findById(userId).orElse(null);
        if (existingUser == null) {
            throw new Exception(Constant.USER_NOT_FOUND);
        } else {
            existingUser.setName(request.getName());
            existingUser.setEmail(request.getEmail());
            existingUser.setAddress(request.getAddress());
            existingUser.setPassword(encoder.encode(request.getPassword()));
            existingUser.setRole(request.getRole());
            existingUser.setPhone(request.getPhone());
            existingUser.setActive(request.isActive());

            return userRepository.save(existingUser);
        }
    }

    public User changeStatus(@NonNull String userId) throws Exception {
        var existingUser = userRepository.findById(userId).orElse(null);
        if (existingUser == null) {
            throw new Exception(Constant.USER_NOT_FOUND);
        } else {
            existingUser.setActive(!existingUser.isActive());

            return userRepository.save(existingUser);
        }
    }

    public List<User> getAllUsers(int page) {
        return userRepository.findAll().subList(page * 10, (page + 1) * 10);
    }

    public User getUserById(@NonNull String userId) {
        return userRepository.findById(userId).get();
    }

}
