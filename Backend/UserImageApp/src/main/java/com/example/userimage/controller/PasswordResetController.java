package com.example.userimage.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.userimage.model.User;
import com.example.userimage.service.EmailService;
import com.example.userimage.service.UserService;

@RestController
@RequestMapping("/api/public")
public class PasswordResetController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/forgot-password")
    public ResponseEntity<?> handleForgotPassword(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        User user = userService.findByEmail(email);
        if (user == null) {
            return ResponseEntity.badRequest().body("Email non trovata.");
        }
        
        
        String token = userService.createPasswordResetTokenForUser(user);
        String resetUrl = "http://localhost:4200/reset-password?token=" + token;
        emailService.sendEmail(user.getEmail(), "Reset della Password", "Clicca qui per reset della password: " + resetUrl);
        
        return ResponseEntity.ok("Controlla la tua email per le istruzioni di reset della password.");
    }
    
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> payload) {
        String token = payload.get("token");
        String newPassword = payload.get("newPassword");

        User user = userService.findByPasswordResetToken(token);
        if (user == null) {
            return ResponseEntity.badRequest().body("Token non valido.");
        }

        userService.updatePassword(user, newPassword);
        return ResponseEntity.ok("Password aggiornata con successo.");
    }
}
