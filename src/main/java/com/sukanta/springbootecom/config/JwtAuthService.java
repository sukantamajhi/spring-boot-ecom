package com.sukanta.springbootecom.config;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.sukanta.springbootecom.model.user.User;
import com.sukanta.springbootecom.repository.userRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtAuthService {
    private static final long EXPIRATION_TIME = 864_000_000;
    private final userRepository userRepository;
    @Value("${SECRET_KEY}")
    private String SECRET_KEY;

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

        return Jwts.builder().setHeaderParam("typ", "JWT").claim("email", email)
                .claim("userId", userId).setIssuedAt(now).setExpiration(expiryDate)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256).compact();
    }

    public User getUser(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(getSignInKey()).build()
                .parseClaimsJws(token).getBody();

        String email = (String) claims.get("email");

        return userRepository.findByEmail(email).orElse(null);
    }

    public String getUserId(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(getSignInKey()).build()
                .parseClaimsJws(token).getBody();

        return String.valueOf(claims.get("userId"));
    }

    private boolean validateToken(String token) throws Exception {
        try {
            Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public boolean verifyJWT(String authToken) throws Exception {
        return !validateToken(authToken);
    }
}
