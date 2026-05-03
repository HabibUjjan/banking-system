package com.bank.bankingsystem.service;

// UserService.java


import com.bank.bankingsystem.dto.LoginRequest;
import com.bank.bankingsystem.dto.RegisterRequest;
import com.bank.bankingsystem.dto.request.ChangePasswordRequest;
import com.bank.bankingsystem.dto.request.UpdateProfileRequest;
import com.bank.bankingsystem.dto.response.JwtResponse;
import com.bank.bankingsystem.dto.response.UserResponse;
import com.bank.bankingsystem.entity.User;

public interface UserService {
    void register(RegisterRequest request);
    JwtResponse login(LoginRequest request);
    UserResponse getCurrentUser(String username);
    UserResponse updateProfile(String username, UpdateProfileRequest request);
    void changePassword(String username, ChangePasswordRequest request);
    User getUserByUsername(String username);
    User getUserById(Long id);
}
