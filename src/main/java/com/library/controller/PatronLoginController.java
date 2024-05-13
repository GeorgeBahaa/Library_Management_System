package com.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.library.config.JwtUtils;
import com.library.exception.PatronException;
import com.library.model.Patron;
import com.library.service.PatronLoginService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/patrons")
@RequiredArgsConstructor
public class PatronLoginController {

    @Autowired
    private PatronLoginService patronLoginService;

    @Autowired
    private JwtUtils jwtUtil;

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
        try {
            if (!patronLoginService.login(patron.getEmail(), patron.getPassword())) {
                throw new BadCredentialsException("Invalid username or password");
            }
            return ResponseEntity.ok(jwtUtil.generateToken(patron.getEmail()));
        } catch (UsernameNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Login failed: Username not found");
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
