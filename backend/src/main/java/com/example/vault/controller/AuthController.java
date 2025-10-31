package com.example.vault.controller;

import com.example.vault.dto.AuthRequest;
import com.example.vault.dto.AuthResponse;
import com.example.vault.entity.User;
import com.example.vault.security.JwtUtils;
import com.example.vault.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

   @PostMapping("/register")
public ResponseEntity<?> register(@RequestBody AuthRequest req) {
    try {
        User u = userService.register(req.getEmail(), req.getPassword());
        return ResponseEntity.ok(new AuthResponse("registered"));
    } catch (Exception e) {
        if (e.getMessage().contains("ConstraintViolationException") || 
            e.getMessage().contains("Duplicate entry")) {
            return ResponseEntity.status(400).body("Email already registered!");
        }
        return ResponseEntity.status(500).body("Registration failed");
    }
}


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest req) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));
            String token = jwtUtils.generateToken(req.getEmail());
            return ResponseEntity.ok(new AuthResponse(token));
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }
}
