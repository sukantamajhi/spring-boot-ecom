package com.sukanta.springbootecom.service;

import com.sukanta.springbootecom.config.Constant;
import com.sukanta.springbootecom.config.JwtAuthService;
import com.sukanta.springbootecom.model.User;
import com.sukanta.springbootecom.model.enums.Role;
import com.sukanta.springbootecom.repository.userRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class userService {
    private final Logger log = LoggerFactory.getLogger(userService.class);
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
    private final userRepository userRepository;
    private final JwtAuthService jwtAuthService;

    public userService(userRepository userRepository, JwtAuthService jwtAuthService) {
        this.userRepository = userRepository;
        this.jwtAuthService = jwtAuthService;
    }

    public User registerUser(User request) throws Exception {
        var ExistingUser = userRepository.findByEmail(request.getEmail()).orElse(null);

        if (ExistingUser == null) {
            var user = User.builder()
                    .id(request.getId())
                    .name(request.getName())
                    .email(request.getEmail())
                    .phone(request.getPhone())
                    .address(request.getAddress())
                    .role(Role.USER)
                    .password(encoder.encode(request.getPassword()))
                    .build();
            try {
                return userRepository.save(user);
            } catch (Exception err) {
                log.error(err + " <<== Error in saving data to database");
                throw new Exception(Constant.USER_CREATE_FAILED);
            }
        } else {
            throw new Exception(Constant.USER_EXIST);
        }
    }

    public String login(User request) throws Exception {
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
}
