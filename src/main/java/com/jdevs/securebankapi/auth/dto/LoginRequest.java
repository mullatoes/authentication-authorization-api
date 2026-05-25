package com.jdevs.securebankapi.auth.dto;

import com.mullatoez.security.logger.core.Sensitive;
import com.mullatoez.security.logger.core.SensitiveMode;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(

        @Sensitive(mode = SensitiveMode.PARTIAL)
        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        String email,

        @Sensitive(mode = SensitiveMode.FULL)
        @NotBlank(message = "Password is required")
        String password
) {
}
