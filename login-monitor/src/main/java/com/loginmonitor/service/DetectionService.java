
package com.loginmonitor.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.loginmonitor.entity.LoginAttempt;
import com.loginmonitor.entity.LoginStatus;
import com.loginmonitor.entity.SuspiciousActivity;

import com.loginmonitor.repository.LoginAttemptRepository;
import com.loginmonitor.repository.SuspiciousActivityRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DetectionService {

    private final LoginAttemptRepository loginAttemptRepository;
    private final SuspiciousActivityRepository suspiciousActivityRepository;

    private static final int FAILURE_THRESHOLD = 5;
    private static final int SUCCESS_THRESHOLD = 3;

    private static final int TIME_WINDOW = 10;

    public void detectSuspiciousActivity(LoginAttempt attempt) {

        LocalDateTime timeLimit =
                LocalDateTime.now().minusMinutes(TIME_WINDOW);

        String ipAddress = attempt.getIpAddress();
        String username = attempt.getUsername();

        // Count failures from same IP
        long ipFailures =
                loginAttemptRepository
                .countByIpAddressAndStatusAndAttemptedAtAfter(
                    ipAddress,
                    LoginStatus.FAILURE,
                    timeLimit
                );

        // Count failures for same username
        long userFailures =
                loginAttemptRepository
                .countByUsernameAndStatusAndAttemptedAtAfter(
                    username,
                    LoginStatus.FAILURE,
                    timeLimit
                );

        // Brute force detection by IP
        if (ipFailures >= FAILURE_THRESHOLD) {

            saveSuspiciousActivity(
                ipAddress,
                null,
                "5 or more failed logins from same IP within 10 minutes"
            );
        }

        // Brute force detection by username
        if (userFailures >= FAILURE_THRESHOLD) {

            saveSuspiciousActivity(
                ipAddress,
                username,
                "5 or more failed logins for same username within 10 minutes"
            );
        }

        // Unusual successful login detection
        if (attempt.getStatus() == LoginStatus.SUCCESS) {

            if (ipFailures >= SUCCESS_THRESHOLD ||
                userFailures >= SUCCESS_THRESHOLD) {

                saveSuspiciousActivity(
                    ipAddress,
                    username,
                    "Successful login following multiple failed attempts"
                );
            }
        }
    }

    private void saveSuspiciousActivity(
            String ipAddress,
            String username,
            String reason) {

        LocalDateTime now = LocalDateTime.now();

        // Avoid duplicate alerts for the same reason
        boolean alreadyExists =
                suspiciousActivityRepository
                .existsByIpAddressAndReasonAndCreatedAtAfter(
                    ipAddress,
                    reason,
                    now.minusMinutes(TIME_WINDOW)
                );

        if (alreadyExists) {
            return;
        }

        SuspiciousActivity activity = new SuspiciousActivity();

        activity.setIpAddress(ipAddress);
        activity.setUsername(username);
        activity.setReason(reason);
        activity.setCreatedAt(now);

        suspiciousActivityRepository.save(activity);
    }
}
