package com.example.Ticketing_project.controller;

import com.example.Ticketing_project.dto.JwtResponse;
import com.example.Ticketing_project.dto.LoginRequest;
import com.example.Ticketing_project.dto.RegisterRequest;
import com.example.Ticketing_project.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // POST: /api/auth/register
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody RegisterRequest request) {
        String response = authService.registerUser(request);
        return ResponseEntity.ok(response);
    }

    // POST: /api/auth/login
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest request) {
        JwtResponse response = authService.authenticateUser(request);
        return ResponseEntity.ok(response);
    }
}