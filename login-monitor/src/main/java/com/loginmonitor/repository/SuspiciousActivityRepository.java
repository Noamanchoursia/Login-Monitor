
package com.loginmonitor.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;

import com.loginmonitor.entity.SuspiciousActivity;

public interface SuspiciousActivityRepository
        extends JpaRepository<SuspiciousActivity, Long> {

    boolean existsByIpAddressAndReasonAndCreatedAtAfter(
            String ipAddress,
            String reason,
            LocalDateTime time
    );
}
