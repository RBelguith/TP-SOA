package com.ranimbelguith.TP_SOA.config;

import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, JwtDecoder jwtDecoder) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Page d'erreur interne de Boot : doit rester accessible pour
                        // ne pas transformer un 404/400 anonyme en 401
                        .requestMatchers("/error").permitAll()
                        // Page d'accueil (documentation de l'API)
                        .requestMatchers(HttpMethod.GET, "/").permitAll()
                        // Récupération du jeton : accessible sans authentification
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                        // Toutes les méthodes GET de l'API : publiques
                        .requestMatchers(HttpMethod.GET, "/api/**").permitAll()
                        // Les autres opérations CRUD exigent un jeton JWT
                        .anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.decoder(jwtDecoder)))
                .build();
    }

    @Bean
    UserDetailsService userDetailsService(
            PasswordEncoder passwordEncoder,
            @Value("${app.security.username:user}") String username,
            @Value("${app.security.password:user123}") String password) {
        return new InMemoryUserDetailsManager(User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .roles("USER")
                .build());
    }

    @Bean
    JwtDecoder jwtDecoder(@Value("${app.security.jwt.secret}") String secretBase64) {
        SecretKey secretKey = new SecretKeySpec(
                Base64.getDecoder().decode(secretBase64), "HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(secretKey).build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
