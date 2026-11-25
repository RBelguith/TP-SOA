package com.ranimbelguith.TP_SOA.controller;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.ranimbelguith.TP_SOA.security.JwtService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(UserDetailsService userDetailsService,
                          PasswordEncoder passwordEncoder,
                          JwtService jwtService) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    /**
     * Authentifie l'utilisateur (HTTP Basic) et retourne un jeton JWT.
     * POST /api/auth/login avec l'en-tête : Authorization: Basic base64(username:password)
     */
    @PostMapping("/login")
    public LoginResponse login(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorization) {

        if (authorization == null || !authorization.startsWith("Basic ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentification Basic requise");
        }

        try {
            String decoded = new String(
                    Base64.getDecoder().decode(authorization.substring(6)), StandardCharsets.UTF_8);

            int separator = decoded.indexOf(':');
            if (separator < 0) {
                throw new IllegalArgumentException("En-tête Basic invalide");
            }

            String username = decoded.substring(0, separator);
            String password = decoded.substring(separator + 1);

            UserDetails user = userDetailsService.loadUserByUsername(username);
            if (!passwordEncoder.matches(password, user.getPassword())) {
                throw new UsernameNotFoundException("Identifiants invalides");
            }

            return new LoginResponse(jwtService.generateToken(user.getUsername()), "Bearer");

        } catch (IllegalArgumentException | AuthenticationException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Identifiants invalides");
        }
    }

    public record LoginResponse(String token, String tokenType) {
    }
}
