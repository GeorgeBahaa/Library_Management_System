package com.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.library.exception.PatronException;
import com.library.model.Patron;
import com.library.service.PatronService;

@RestController
@RequestMapping("/api/patrons")
public class PatronController {

    @Autowired
    private PatronService patronService;

    @GetMapping
    public ResponseEntity<?> getAllPatrons() {
        return ResponseEntity.ok(patronService.getAllPatrons());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPatronById(@PathVariable Integer id) {
        try {
            Patron patron = patronService.getPatronById(id);
            return ResponseEntity.ok(patron);
        } catch (PatronException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> addPatron(@RequestBody Patron patron) {
        return ResponseEntity.status(HttpStatus.CREATED).body(patronService.addPatron(patron));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePatron(@PathVariable Integer id, @RequestBody Patron patron) {
        try {
            return ResponseEntity.ok(patronService.updatePatron(id, patron));
        } catch (PatronException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePatron(@PathVariable Integer id) {
        try {
            patronService.deletePatron(id);
            return ResponseEntity.noContent().build();
        } catch (PatronException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }
}
