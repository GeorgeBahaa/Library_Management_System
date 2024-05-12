package com.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.library.exception.PatronException;
import com.library.model.Patron;
import com.library.repository.PatronRepository;
import com.library.security.PatronDetails;

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

    public UserDetails login(String email, String password) {
        Patron patron = patronRepository.findByEmail(email);
        if (patron != null && passwordEncoder.matches(password, patron.getPassword())) {
            return new PatronDetails(patron);
        } else {
            throw new PatronException("Invalid email or password");
        }
    }

}
