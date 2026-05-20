package com.jdevs.securebankapi.auth.token;

import com.jdevs.securebankapi.user.security.AppUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
public class JwtService {

    private final JwtProperties jwtProperties;
    private final SecretKey signingKey;

    public JwtService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.signingKey = Keys.hmacShaKeyFor(
                jwtProperties.secret().getBytes(StandardCharsets.UTF_8)
        );
    }

    public String generateAccessToken(AppUserDetails userDetails) {

        Instant now = Instant.now();

        Instant expirationTime = now.plusSeconds(
                jwtProperties.accessTokenExpirationMinutes() * 60
        );

        List<String> authorities = userDetails.getAuthorities()
                .stream()
                .map(authority -> authority.getAuthority())
                .toList();

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuer(jwtProperties.issuer())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expirationTime))
                .claim("userId", userDetails.getId())
                .claim("fullName", userDetails.getFullName())
                .claim("authorities", authorities)
                .signWith(signingKey)
                .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .requireIssuer(jwtProperties.issuer())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public boolean isTokenValid(String token, AppUserDetails userDetails) {
        String username = extractUsername(token);

        return username.equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        Date expiration = extractClaims(token).getExpiration();
        return expiration.before(new Date());
    }

    public long getAccessTokenExpirationSeconds() {
        return jwtProperties.accessTokenExpirationMinutes() * 60;
    }
}