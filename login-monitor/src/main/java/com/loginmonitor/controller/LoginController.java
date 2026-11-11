
package com.loginmonitor.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.loginmonitor.dto.LoginRequest;
import com.loginmonitor.entity.LoginAttempt;
import com.loginmonitor.entity.LoginStatus;
import com.loginmonitor.service.LoginService;
import com.loginmonitor.service.RateLimitService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private final RateLimitService rateLimitService;

    // POST /login
    @PostMapping
    public ResponseEntity<LoginAttempt> createLoginAttempt(
            @Valid @RequestBody LoginRequest request) {

        // Check rate limit
        rateLimitService.checkRateLimit(request.getIpAddress());

        // Convert DTO into Entity
        LoginAttempt attempt = new LoginAttempt();

        attempt.setUsername(request.getUsername());
        attempt.setIpAddress(request.getIpAddress());
        attempt.setStatus(request.getStatus());

        // Save login attempt
        LoginAttempt savedAttempt =
                loginService.saveLoginAttempt(attempt);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedAttempt);
    }

    @GetMapping
    public ResponseEntity<List<LoginAttempt>> getLoginAttempts(

            @RequestParam(required = false) String username,

            @RequestParam(required = false) String ipAddress,

            @RequestParam(required = false) LoginStatus status) {

        List<LoginAttempt> attempts =
                loginService.getFilteredLoginAttempts(
                        username,
                        ipAddress,
                        status
                );

        return ResponseEntity.ok(attempts);
    }
}
