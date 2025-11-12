package com.harry.order.controller;

import com.harry.order.common.JwtTokenUtil;
import com.harry.order.model.po.User;
import com.harry.order.model.dto.LoginRequest;
import com.harry.order.repository.UserRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Slf4j
@Tag(name = "Authentication API", description = "Login with username, phone, or email")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final JwtTokenUtil jwtTokenUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Secure login endpoint with BCrypt verification.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest request) {
        String loginId = request.getLoginId();
        String password = request.getPassword();

        log.info("Login attempt for identifier: {}", loginId);

        // 1. Try to find user by username, phone, or email
        User user = userRepository.findByUsername(loginId)
                .or(() -> userRepository.findByPhone(loginId))
                .or(() -> userRepository.findByEmail(loginId))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        // 2. Verify password using BCrypt
        if (!passwordEncoder.matches(password, user.getPassword())) {
            log.warn("Invalid password attempt for user: {}", loginId);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        // 3. Generate JWT with user info
        String token = jwtTokenUtil.generateToken(user.getId(), user.getUsername());
        log.info("User [{}] logged in successfully", user.getUsername());

        // 4. Return token and basic user info
        return ResponseEntity.ok(Map.of(
                "token", token,
                "user", Map.of(
                        "id", user.getId(),
                        "username", user.getUsername(),
                        "nickname", user.getNickname(),
                        "email", user.getEmail(),
                        "phone", user.getPhone()
                )
        ));
    }
}
