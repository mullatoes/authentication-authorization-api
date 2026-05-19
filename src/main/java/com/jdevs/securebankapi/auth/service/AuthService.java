package com.jdevs.securebankapi.auth.service;

import com.jdevs.securebankapi.auth.dto.RegisterRequest;
import com.jdevs.securebankapi.auth.dto.RegisterResponse;
import com.jdevs.securebankapi.user.entity.AppUser;
import com.jdevs.securebankapi.user.entity.Role;
import com.jdevs.securebankapi.user.repository.AppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            AppUserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
}
