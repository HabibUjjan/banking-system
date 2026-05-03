package com.bank.bankingsystem.controller;// AuthController.java

import com.bank.bankingsystem.dto.ApiResponse;
import com.bank.bankingsystem.dto.LoginRequest;
import com.bank.bankingsystem.dto.RegisterRequest;
import com.bank.bankingsystem.dto.response.JwtResponse;
import com.bank.bankingsystem.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> register(@Valid @RequestBody RegisterRequest request) {
        userService.register(request);
        return ResponseEntity.ok(ApiResponse.success("Registration successful", null));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtResponse>> login(@Valid @RequestBody LoginRequest request) {
        JwtResponse jwtResponse = userService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Login successful", jwtResponse));
    }
}