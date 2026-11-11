
package com.loginmonitor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.loginmonitor.entity.LoginAttempt;
import com.loginmonitor.entity.LoginStatus;

public interface LoginAttemptRepository
        extends JpaRepository<LoginAttempt, Long> {

    long countByIpAddressAndStatusAndAttemptedAtAfter(
            String ipAddress,
            LoginStatus status,
            java.time.LocalDateTime time);

    long countByUsernameAndStatusAndAttemptedAtAfter(
            String username,
            LoginStatus status,
            java.time.LocalDateTime time);

    @Query("""
        SELECT l FROM LoginAttempt l
        WHERE (:username IS NULL OR l.username = :username)
        AND (:ipAddress IS NULL OR l.ipAddress = :ipAddress)
        AND (:status IS NULL OR l.status = :status)
        ORDER BY l.attemptedAt DESC
        """)
    List<LoginAttempt> searchAttempts(
            @Param("username") String username,
            @Param("ipAddress") String ipAddress,
            @Param("status") LoginStatus status);
}
