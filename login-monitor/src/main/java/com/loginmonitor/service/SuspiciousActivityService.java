package com.loginmonitor.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.loginmonitor.entity.SuspiciousActivity;
import com.loginmonitor.repository.SuspiciousActivityRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SuspiciousActivityService {

    private final SuspiciousActivityRepository repository;

    public List<SuspiciousActivity> getAllSuspiciousActivity() {
        return repository.findAll();
    }
}