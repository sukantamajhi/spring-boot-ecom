package com.sukanta.springbootecom.config;

import com.sukanta.springbootecom.model.User;
import com.sukanta.springbootecom.repository.userRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtAuthService {
    private static final String SECRET_KEY = "MTkH3MnbXRe5OAd164KR96l9MjObF/Se339B0BKRy9Dy+MGotE+oOwM/YbtFoQzKqJpccn47AX4h7VuTS4kV5Q=="; // Change this with a strong secret key
    private static final long EXPIRATION_TIME = 864_000_000; // 10 days in milliseconds
    private final Logger logger = LoggerFactory.getLogger(JwtAuthService.class);

    private final userRepository userRepository;

    public JwtAuthService(userRepository userRepository) {
        this.userRepository = userRepository;
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String email, String userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder().setHeaderParam("typ", "JWT").claim("email", email).claim("userId", userId).setIssuedAt(now).setExpiration(expiryDate).signWith(getSignInKey(), SignatureAlgorithm.HS256).compact();
    }

    public User getUser(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();

        String email = (String) claims.get("email");

        return userRepository.findByEmail(email).orElse(null);
    }

    public String getUserId(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();

        return String.valueOf(claims.get("userId"));
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean verifyJWT(String authToken) throws Exception {
        return !validateToken(authToken);
    }
}
