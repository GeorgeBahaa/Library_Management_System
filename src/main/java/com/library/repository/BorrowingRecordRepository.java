package com.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.library.model.BorrowingRecord;

@Repository
public interface BorrowingRecordRepository extends JpaRepository<BorrowingRecord, Integer>{

    void deleteByBookId(Integer id);

    void deleteByPatronId(Integer id);

    BorrowingRecord findByBookIdAndPatronIdAndReturnDateIsNull(Integer bookId, Integer patronId);

}
