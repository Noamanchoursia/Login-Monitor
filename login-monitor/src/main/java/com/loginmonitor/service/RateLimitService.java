
package com.loginmonitor.service;

import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class RateLimitService {

    private static final int MAX_ATTEMPTS = 10;

    private static final long TIME_WINDOW = 60;

    private final ConcurrentHashMap<String, Deque<Instant>>
            attemptsMap = new ConcurrentHashMap<>();

    public void checkRateLimit(String ipAddress) {

        Instant now = Instant.now();

        Deque<Instant> attempts =
                attemptsMap.computeIfAbsent(
                    ipAddress,
                    key -> new ArrayDeque<>()
                );

        synchronized (attempts) {

            // Remove expired attempts
            while (!attempts.isEmpty() &&
                    attempts.peekFirst()
                    .isBefore(now.minusSeconds(TIME_WINDOW))) {

                attempts.pollFirst();
            }

            // Check maximum attempts
            if (attempts.size() >= MAX_ATTEMPTS) {

                throw new ResponseStatusException(
                    HttpStatus.TOO_MANY_REQUESTS,
                    "Too many login attempts. Try again later."
                );
            }

            // Record current attempt
            attempts.addLast(now);
        }
    }
}
