package com.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.library.model.Patron;

@Repository
public interface PatronRepository extends JpaRepository<Patron, Integer> {

    Patron findByEmail(String email);

}
