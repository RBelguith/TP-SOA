package com.ranimbelguith.TP_SOA.security;

import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JwtServiceTest {

    private static final String SECRET = "MDEyMzQ1Njc4OWFiY2RlZjAxMjM0NTY3ODlhYmNkZWY=";

    @Test
    void generateTokenShouldReturnSignedJwtWithSubject() {
        JwtService jwtService = new JwtService(SECRET, 180);

        String token = jwtService.generateToken("Ranim");

        assertNotNull(token);
        Jwt jwt = decoder().decode(token);
        assertEquals("Ranim", jwt.getSubject());
        assertEquals("tp-soa", jwt.getClaimAsString("iss"));
        assertNotNull(jwt.getExpiresAt());
    }

    @Test
    void tamperedTokenShouldBeRejected() {
        JwtService jwtService = new JwtService(SECRET, 180);
        String token = jwtService.generateToken("Ranim");
        String tamperedToken = token.substring(0, token.length() - 4) + "AAAA";

        assertThrows(Exception.class, () -> decoder().decode(tamperedToken));
    }

    private JwtDecoder decoder() {
        SecretKey key = new SecretKeySpec(Base64.getDecoder().decode(SECRET), "HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(key).build();
    }
}
