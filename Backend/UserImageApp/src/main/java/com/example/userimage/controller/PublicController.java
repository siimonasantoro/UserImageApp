package com.example.userimage.controller;

import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.userimage.dto.ResponseDTO;
import com.example.userimage.dto.SignInDTO;
import com.example.userimage.dto.SignUpDTO;
import com.example.userimage.model.User;
import com.example.userimage.service.UserService;
import com.example.userimage.jwt.JwtProvider;

@RestController
@RequestMapping("/api/public")
public class PublicController {

    @Autowired
    private UserService userService; 

    @Autowired
    private JwtProvider jwtProvider; 

    @PostMapping("/sign-in")
    public ResponseEntity<ResponseDTO> signIn(@RequestBody SignInDTO signInDTO) {
        System.out.println("Tentativo di accesso per l'utente: " + signInDTO.getUsername());
        User authenticatedUser = userService.authenticate(signInDTO.getUsername(), signInDTO.getPassword());

        if (authenticatedUser != null) {
            try {
                String token = jwtProvider.generateJwtToken(authenticatedUser.getUsername(), new HashMap<>());


                ResponseDTO responseDto = new ResponseDTO("Login successful", authenticatedUser, token);
                
                System.out.println("ResponseDTO: " + responseDto);

                return ResponseEntity.ok(responseDto);
            } catch (Exception e) {
                System.err.println("Errore durante la generazione del token: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ResponseDTO(false, "Token generation failed", e.getMessage()));
            }
        } else {
            System.err.println("Autenticazione fallita per l'utente: " + signInDTO.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseDTO(false, "Invalid credentials", "Autenticazione fallita"));
        }
    }

    @PostMapping("/sign-up")
    public ResponseEntity<ResponseDTO> signUp(@RequestBody SignUpDTO signUpDTO) {
        User registeredUser = userService.registerUser(signUpDTO); // Registrazione dell'utente
        
        if (registeredUser != null) {
            return ResponseEntity.ok(new ResponseDTO("success", registeredUser));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO(false, "Registration failed", "Controlla i dati di input"));
        }
    }
}
