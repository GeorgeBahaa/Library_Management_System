package com.library.controller;

import java.security.Key;
import java.util.Collections;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.library.exception.PatronException;
import com.library.model.Patron;
import com.library.service.PatronLoginService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@RestController
@RequestMapping("/api/patrons")
public class PatronLoginController {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private int jwtExpirationMs;

    @Autowired
    private PatronLoginService patronLoginService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public PatronLoginController(PatronLoginService patronLoginService, AuthenticationManager authenticationManager) {
        this.patronLoginService = patronLoginService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerPatron(@RequestBody Patron patron) {
        try {
            return ResponseEntity.ok(patronLoginService.registerPatron(patron));
        } catch (PatronException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Patron patron) {
        if (patron.getEmail() == null || patron.getPassword() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("username and password is required");
        }

        Patron loginPatron = patronLoginService.login(patron.getEmail(), patron.getPassword());
        if (loginPatron != null) {
            try {
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(patron.getEmail(), patron.getPassword()));

                SecurityContextHolder.getContext().setAuthentication(authentication);

                // If authentication is successful, generate JWT token
                String token = Jwts.builder()
                        .setSubject(patron.getEmail())
                        .setIssuedAt(new Date())
                        .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                        .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                        .compact();

                return ResponseEntity.ok(Collections.singletonMap("token", token));
            } catch (UsernameNotFoundException ex) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Login failed: Username not found");
            } catch (AuthenticationException e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid username or password");

        }
    }

    private Key getSigningKey() {
        // Generate a secure key for HS256 algorithm
        return Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

}
