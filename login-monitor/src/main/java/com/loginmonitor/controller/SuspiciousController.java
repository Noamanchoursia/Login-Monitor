
package com.loginmonitor.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.loginmonitor.entity.SuspiciousActivity;
import com.loginmonitor.repository.SuspiciousActivityRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/suspicious")
@RequiredArgsConstructor
public class SuspiciousController {

    private final SuspiciousActivityRepository suspiciousActivityRepository;

    // GET /suspicious
    @GetMapping
    public ResponseEntity<List<SuspiciousActivity>> getAllSuspiciousActivity() {

        List<SuspiciousActivity> activities =
                suspiciousActivityRepository.findAll();

        return ResponseEntity.ok(activities);
    }
}
