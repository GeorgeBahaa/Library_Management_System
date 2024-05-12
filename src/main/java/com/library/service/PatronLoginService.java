package com.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.library.exception.PatronException;
import com.library.model.Patron;
import com.library.repository.PatronRepository;

@Service
public class PatronLoginService {

    @Autowired
    private PatronRepository patronRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Patron registerPatron(Patron patron) {
        try{
            patron.setPassword(passwordEncoder.encode(patron.getPassword()));
        return patronRepository.save(patron);
        } catch (DataIntegrityViolationException e){
            throw new PatronException("Failed to register the Patron.");
        }
    }

    public Patron login(String email, String password) {
        Patron patron = patronRepository.findByEmail(email);
        if (patron != null && passwordEncoder.matches(password, patron.getPassword())) {
            return patron;
        } else {
            throw new PatronException("Invalid email or password");
        }
    }

}
