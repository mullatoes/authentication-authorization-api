package com.jdevs.securebankapi.auth.service;

import com.jdevs.securebankapi.auth.dto.LoginRequest;
import com.jdevs.securebankapi.auth.dto.LoginResponse;
import com.jdevs.securebankapi.auth.dto.RegisterRequest;
import com.jdevs.securebankapi.auth.dto.RegisterResponse;
import com.jdevs.securebankapi.user.entity.AppUser;
import com.jdevs.securebankapi.user.entity.Role;
import com.jdevs.securebankapi.user.repository.AppUserRepository;
import com.jdevs.securebankapi.user.security.AppUserDetails;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthService(
            AppUserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public RegisterResponse register(RegisterRequest request) {

        String normalizedEmail = request.email().trim().toLowerCase();

        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new IllegalArgumentException("Email is already registered");
        }

        String passwordHash = passwordEncoder.encode(request.password());

        AppUser user = new AppUser(
                request.fullName().trim(),
                normalizedEmail,
                passwordHash,
                Role.CUSTOMER
        );

        AppUser savedUser = userRepository.save(user);

        return new RegisterResponse(
                savedUser.getId(),
                savedUser.getFullName(),
                savedUser.getEmail(),
                savedUser.getRole().name()
        );
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {

        String normalizedEmail = request.email().trim().toLowerCase();

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        normalizedEmail,
                        request.password()
                )
        );

        AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();

        return new LoginResponse(
                "token-will-come-in-next-step",
                "Bearer",
                900L
        );
    }
}