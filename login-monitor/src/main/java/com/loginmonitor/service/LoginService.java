
package com.loginmonitor.service;

import java.util.List;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loginmonitor.entity.LoginAttempt;
import com.loginmonitor.entity.LoginStatus;
import com.loginmonitor.repository.LoginAttemptRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final LoginAttemptRepository loginAttemptRepository;
    private final DetectionService detectionService;

    // Save login attempt
    @Transactional
    public LoginAttempt saveLoginAttempt(LoginAttempt loginAttempt) {

        loginAttempt.setAttemptedAt(LocalDateTime.now());

        LoginAttempt savedAttempt =
                loginAttemptRepository.save(loginAttempt);

        // Check suspicious activity
        detectionService.detectSuspiciousActivity(savedAttempt);

        return savedAttempt;
    }

    // Get all login attempts
    public List<LoginAttempt> getAllLoginAttempts() {

        return loginAttemptRepository.findAll();

}
    public List<LoginAttempt> getFilteredLoginAttempts(
            String username,
            String ipAddress,
            LoginStatus status) {

        return loginAttemptRepository.searchAttempts(
                username,
                ipAddress,
                status
        );
    }
}
