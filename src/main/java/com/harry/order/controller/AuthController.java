package com.harry.order.controller;

import com.harry.order.common.JwtTokenUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@Tag(name = "Authentication API", description = "Login")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final JwtTokenUtil jwtTokenUtil;

    /**
     * temporary method
     * @param username
     * @param password
     * @return
     */
    @PostMapping("/login")
    public Map<String, String> login(@RequestParam String username, @RequestParam String password) {
        // TODO: Replace with actual authentication
        Long mockUserId = 1L;
        String mockUserKey = "mock-user-001";

        String token = jwtTokenUtil.generateToken(mockUserId, mockUserKey);
        return Map.of("token", token);
    }
}
