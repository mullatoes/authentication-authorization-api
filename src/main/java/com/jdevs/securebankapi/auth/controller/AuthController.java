package com.jdevs.securebankapi.auth.controller;

import com.jdevs.securebankapi.auth.dto.LoginRequest;
import com.jdevs.securebankapi.auth.dto.LoginResponse;
import com.jdevs.securebankapi.auth.dto.RegisterRequest;
import com.jdevs.securebankapi.auth.dto.RegisterResponse;
import com.jdevs.securebankapi.auth.service.AuthService;
import com.jdevs.securebankapi.shared.response.ApiResponse;
import com.mullatoez.security.logger.spring.MullatoezSensitiveLogger;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final MullatoezSensitiveLogger sensitiveLogger;

    public AuthController(
            AuthService authService, MullatoezSensitiveLogger sensitiveLogger
    ) {
        this.authService = authService;
        this.sensitiveLogger = sensitiveLogger;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponse>> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        RegisterResponse response = authService.register(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("User registered successfully", response));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request
    ) {
        log.info("Incoming login request={}", sensitiveLogger.mask(request));
        LoginResponse response = authService.login(request);

        return ResponseEntity.ok(
                ApiResponse.success("Login successful", response)
        );
    }
}
