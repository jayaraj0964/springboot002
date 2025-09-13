package com.studentspring.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.studentspring.config.JwtUtil;
import com.studentspring.dto.AuthRequest;
import com.studentspring.dto.AuthResponse;
import com.studentspring.dto.RegisterRequest;
import com.studentspring.entity.AppUser;
import com.studentspring.repository.UserRepository;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        logger.info("📥 Register request received for username: {}", request.getUsername());

        if (request.getUsername() == null || request.getPassword() == null || request.getRole() == null) {
            logger.warn("❌ Missing fields in request: {}", request);
            return ResponseEntity.badRequest().body("Missing required fields");
        }

        if (userRepo.findByUsername(request.getUsername()).isPresent()) {
            logger.warn("❌ Username already exists: {}", request.getUsername());
            return ResponseEntity.badRequest().body("Username already exists");
        }

        try {
            AppUser user = new AppUser();
            user.setUsername(request.getUsername());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setRole(request.getRole());

            logger.debug("🔐 Encoded password: {}", user.getPassword());
            userRepo.save(user);
            logger.info("✅ User registered successfully: {}", user.getUsername());

            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            logger.error("❌ Exception during registration", e);
            return ResponseEntity.status(500).body("Registration failed");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        logger.info("📥 Login request received for username: {}", request.getUsername());

        if (request.getUsername() == null || request.getPassword() == null) {
            logger.warn("❌ Missing fields in login request: {}", request);
            return ResponseEntity.badRequest().body("Missing username or password");
        }

        try {
            Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            logger.info("✅ Authentication successful for user: {}", request.getUsername());

            String token = jwtUtil.generateToken(request.getUsername());
            logger.debug("🔐 Generated JWT token for user: {}", request.getUsername());

            return ResponseEntity.ok(new AuthResponse(token));
        } catch (Exception e) {
            logger.error("❌ Authentication failed for user: {}", request.getUsername(), e);
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }
}