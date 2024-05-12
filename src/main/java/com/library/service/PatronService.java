package com.library.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.library.exception.PatronException;
import com.library.model.Patron;
import com.library.repository.BorrowingRecordRepository;
import com.library.repository.PatronRepository;

@Service
public class PatronService {

    @Autowired
    private PatronRepository patronRepository;

    private BorrowingRecordRepository borrowingRecordRepository;

    public List<Patron> getAllPatrons() {
        return patronRepository.findAll();
    }

    public Patron getPatronById(Integer id) {
        return patronRepository.findById(id).orElseThrow(() -> new PatronException("Patron not found with ID: " + id));
    }

    public Patron addPatron(Patron patron) {
        return patronRepository.save(patron);
    }

    public Patron updatePatron(Integer id, Patron updatedPatron) {
        Patron existingPatron = patronRepository.findById(id)
                .orElseThrow(() -> new PatronException("Patron not found with ID: " + id));

        existingPatron.setName(updatedPatron.getName());
        existingPatron.setAddress(updatedPatron.getAddress());
        existingPatron.setPhoneNumber(updatedPatron.getPhoneNumber());
        existingPatron.setEmail(updatedPatron.getEmail());

        return patronRepository.save(existingPatron);
    }

    public void deletePatron(Integer id) {
        if (!patronRepository.existsById(id)) {
            throw new PatronException("Patron not found with ID: " + id);
        }
        borrowingRecordRepository.deleteByPatronId(id);
        patronRepository.deleteById(id);
    }

}
