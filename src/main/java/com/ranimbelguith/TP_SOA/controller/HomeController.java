package com.ranimbelguith.TP_SOA.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Page d'accueil : documente les endpoints de l'API.
 */
@RestController
public class HomeController {

    @GetMapping("/")
    public Map<String, Object> info() {
        return Map.of(
                "application", "TP-SOA - API de gestion des etudiants (Ranim Belguith)",
                "endpoints", Map.of(
                        "login (public)", "POST /api/auth/login  -  Basic Auth Ranim/Ranim1234, retourne un jeton JWT",
                        "lister (public)", "GET /api/students",
                        "detail (public)", "GET /api/students/{id}",
                        "ajouter (JWT)", "POST /api/students  -  Authorization: Bearer <token>",
                        "modifier (JWT)", "PUT /api/students/{id}  -  Authorization: Bearer <token>",
                        "supprimer (JWT)", "DELETE /api/students/{id}  -  Authorization: Bearer <token>"));
    }
}
