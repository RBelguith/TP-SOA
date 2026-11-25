package com.ranimbelguith.TP_SOA.security;

import java.time.Instant;

import javax.crypto.SecretKey;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.stereotype.Service;

/**
 * Génère les jetons JWT (HS256) utilisés pour sécuriser l'API.
 */
@Service
public class JwtService {

    private static final String ISSUER = "tp-soa";

    private final JwtEncoder jwtEncoder;
    private final long expirationSeconds;

    public JwtService(
            @Value("${app.security.jwt.secret}") String secretBase64,
            @Value("${app.security.jwt.expiration-seconds}") long expirationSeconds) {

        SecretKey secretKey = new javax.crypto.spec.SecretKeySpec(
                java.util.Base64.getDecoder().decode(secretBase64), "HmacSHA256");

        JWK jwk = new OctetSequenceKey.Builder(secretKey)
                .keyID("tp-soa-hs256")
                .algorithm(JWSAlgorithm.HS256)
                .build();

        JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(jwk));
        this.jwtEncoder = new NimbusJwtEncoder(jwkSource);
        this.expirationSeconds = expirationSeconds;
    }

    /**
     * Génère un jeton JWT signé pour l'utilisateur donné.
     */
    public String generateToken(String username) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(ISSUER)
                .subject(username)
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expirationSeconds))
                .claim("roles", java.util.List.of("USER"))
                .build();

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256)
                .type("JWT")
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }

    public long getExpirationSeconds() {
        return expirationSeconds;
    }
}
