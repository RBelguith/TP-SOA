package com.ranimbelguith.TP_SOA.controller;

import java.util.Map;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.boot.webmvc.error.ErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Remplace la page d'erreur "Whitelabel" de Spring Boot par une réponse JSON.
 */
@RestController
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        Object statusAttr = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        int status = (statusAttr instanceof Integer i) ? i : 500;

        String message = switch (status) {
            case 400 -> "Requête invalide";
            case 401 -> "Authentification requise (jeton JWT manquant ou invalide)";
            case 404 -> "Ressource introuvable";
            case 405 -> "Méthode HTTP non autorisée pour cette URL";
            default -> "Erreur interne du serveur";
        };

        return ResponseEntity.status(status).body(Map.of(
                "status", status,
                "error", message));
    }
}
