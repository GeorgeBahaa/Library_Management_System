package com.library.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.library.exception.BookException;
import com.library.exception.BorrowingRecordException;
import com.library.exception.PatronException;
import com.library.model.Book;
import com.library.model.BorrowingRecord;
import com.library.model.Patron;
import com.library.repository.BookRepository;
import com.library.repository.BorrowingRecordRepository;
import com.library.repository.PatronRepository;

@Service
public class BorrowingRecordService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PatronRepository patronRepository;

    @Autowired
    private BorrowingRecordRepository borrowingRecordRepository;

    public BorrowingRecord borrowBook(Integer bookId, Integer patronId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookException("Book not found with ID: " + bookId));
        Patron patron = patronRepository.findById(patronId)
                .orElseThrow(() -> new PatronException("Patron not found with ID: " + patronId));

        if (book.getIsBorrowed()) {
            throw new BookException("Book is already borrowed");
        }
        book.setIsBorrowed(true);
        bookRepository.save(book);

        BorrowingRecord borrowingRecord = new BorrowingRecord();
        borrowingRecord.setBook(book);
        borrowingRecord.setPatron(patron);
        borrowingRecord.setBorrowDate(new Date());

        return borrowingRecordRepository.save(borrowingRecord);
    }

    public BorrowingRecord returnBook(Integer bookId, Integer patronId) {
        BorrowingRecord borrowingRecord = borrowingRecordRepository.findByBookIdAndPatronIdAndReturnDateIsNull(bookId,
                patronId);

        if (borrowingRecord != null) {
            Book book = borrowingRecord.getBook();
            book.setIsBorrowed(false);
            bookRepository.save(book);

            borrowingRecord.setReturnDate(new Date());
            return borrowingRecordRepository.save(borrowingRecord);
        } else {
            throw new BorrowingRecordException("This book has already returned or wasn't borrow to this patron");
        }
    }

}
