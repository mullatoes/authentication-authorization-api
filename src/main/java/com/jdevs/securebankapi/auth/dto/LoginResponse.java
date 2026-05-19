package com.jdevs.securebankapi.auth.dto;

public record LoginResponse(
        String accessToken,
        String tokenType,
        Long expiresInSeconds
) {
}
