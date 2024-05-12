package com.library.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.exception.BookException;
import com.library.model.Book;
import com.library.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Test
    public void testGetAllBooks() throws Exception {
        List<Book> books = new ArrayList<>();
        books.add(new Book(1, "Book 1", "George", 2004, "ISBN1", false));
        books.add(new Book(2, "Book 2", "Bahaa", 2005, "ISBN2", false));

        when(bookService.getAllBooks()).thenReturn(books);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/books"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Book 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].author").value("George"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Book 2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].author").value("Bahaa"));
    }

    @Test
    public void testGetBookById() throws Exception {
        Book book = new Book(1, "Book 1", "George", 2004, "ISBN1", false);
        
        when(bookService.getBookById(1)).thenReturn(book);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/books/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Book 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value("George"));
    }

    @Test
    public void testGetBookById_BookNotFound() throws Exception {
        when(bookService.getBookById(1)).thenThrow(new BookException("Book not found with ID: " + 1));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/books/1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("Book not found with ID: " + 1));
    }

    @Test
    public void testAddBook() throws Exception {
        Book book = new Book(1, "Book 1", "George", 2004, "ISBN1", false);
        
        when(bookService.addBook(any(Book.class))).thenReturn(book);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(book)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Book 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value("George"));
    }

    @Test
    public void testUpdateBook() throws Exception {
        Book book = new Book(1, "Book 1", "George", 2004, "ISBN1", false);
        Book updatedBook = new Book(1, "Updated Book", "Bahaa", 2005, "Updated ISBN", false);
        

        when(bookService.updateBook(1, book)).thenReturn(updatedBook);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(book)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Updated Book"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value("Bahaa"));
    }

    @Test
    public void testDeleteBook() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/books/1"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
