package com.jdevs.securebankapi.auth.dto;

public record RegisterResponse(
        Long userId,
        String fullName,
        String email,
        String role
) {
}
