package com.example.moviecollection.controller;

import com.example.moviecollection.model.AppUser;
import com.example.moviecollection.service.AppUserService;
import com.example.moviecollection.service.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AppUserService appUserService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(AppUserService appUserService, PasswordEncoder passwordEncoder,
                          AuthenticationManager authenticationManager, JwtService jwtService) {
        this.appUserService = appUserService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody AppUser user){
        if (user.getPassword() == null || user.getPassword().length() < 6) {
            return ResponseEntity.badRequest().body("Password must be at least 6 characters long");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return ResponseEntity.ok(appUserService.saveUser(user));
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AppUser user) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        );
        String token = jwtService.generateToken(user.getUsername());
        return ResponseEntity.ok(token);
    }

}
