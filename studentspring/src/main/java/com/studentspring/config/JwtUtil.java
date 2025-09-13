package com.studentspring.config;

import java.util.Date;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);
    // Use a secure, randomly generated key (at least 32 bytes for HS256)
    private final String SECRET_KEY = "8Y2jX5kP9mW3qZ7tR4vN6bL2hF8gD1cJ3xK9pQ5wT2rY6mB"; // Example Base64-encoded key

    public String generateToken(String username) {
        logger.info("🔐 Generating JWT for user: {}", username);
        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour expiration
            .signWith(SignatureAlgorithm.HS256, SECRET_KEY.getBytes(StandardCharsets.UTF_8)) // Use byte array
            .compact();
    }

    public String extractUsername(String token) {
        try {
            String username = Jwts.parser()
                .setSigningKey(SECRET_KEY.getBytes(StandardCharsets.UTF_8)) // Use byte array
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
                logger.debug("🔍 Token received: {}", token);
            logger.debug("✅ Extracted username from token: {}", username);
            return username;
        } catch (Exception e) {
            logger.error("❌ Failed to extract username from token", e);
            return null;
        }
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        String extracted = extractUsername(token);
        boolean isValid = extracted != null && extracted.equals(userDetails.getUsername());
        logger.info("🔍 Token validation for {}: {}", userDetails.getUsername(), isValid ? "VALID" : "INVALID");
        return isValid;
    }
    
}
