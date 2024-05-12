package com.library.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.library.exception.BookException;
import com.library.model.Book;
import com.library.repository.BookRepository;
import com.library.repository.BorrowingRecordRepository;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    private BorrowingRecordRepository borrowingRecordRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(Integer id) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        return optionalBook.orElseThrow(() -> new BookException("Book not found with ID: " + id));
    }

    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    public Book updateBook(Integer id, Book updatedBook) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new BookException("Book not found with ID: " + id));

        existingBook.setName(updatedBook.getName());
        existingBook.setAuthor(updatedBook.getAuthor());
        existingBook.setPublicationYear(updatedBook.getPublicationYear());
        existingBook.setISBN(updatedBook.getISBN());

        return bookRepository.save(existingBook);
    }

    public void deleteBook(Integer id) {
        if (!bookRepository.existsById(id)) {
            throw new BookException("Book not found with ID: " + id);
        }
        borrowingRecordRepository.deleteByBookId(id);
        bookRepository.deleteById(id);
    }

}
