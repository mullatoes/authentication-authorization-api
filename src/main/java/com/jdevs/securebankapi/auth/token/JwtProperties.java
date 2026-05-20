package com.jdevs.securebankapi.auth.token;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(
        prefix = "security.jwt"
)
public record JwtProperties(
        String secret,
        String issuer,
        long accessTokenExpirationMinutes
) {
}
