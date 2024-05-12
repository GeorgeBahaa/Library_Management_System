package com.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.library.security.JwtUtils;
import com.library.service.PatronLoginService;

@RestController
@RequestMapping("/api/patrons")
public class PatronLoginController {

    @Autowired
    private JwtUtils jwtUtils;

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

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(patron.getEmail(), patron.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // String jwtToken = jwtUtils.generateToken(authentication);

            return ResponseEntity.ok(authentication);
        } catch (UsernameNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Login failed: Username not found");
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

   

}
