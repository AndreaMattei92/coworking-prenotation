package com.example.coworking_prenotation.controller;

import com.example.coworking_prenotation.dto.userdto.UserRegistrationDTO;
import com.example.coworking_prenotation.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {


    private final AuthService authService;

    public AuthController( AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRegistrationDTO userRegistrationDTO) {
        try {
            String message = authService.registerUser(userRegistrationDTO);
            return ResponseEntity.ok(message);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestParam String Username,
            @RequestParam String Password) {
        try {
            String accessToken = authService.login(Username, Password);
            return ResponseEntity.ok(accessToken);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        try {
            String message = authService.deleteUserById(id);
            return ResponseEntity.ok(message);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
