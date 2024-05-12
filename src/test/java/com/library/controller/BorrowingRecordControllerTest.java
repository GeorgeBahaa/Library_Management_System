package com.library.controller;

import com.library.exception.BookException;
import com.library.exception.BorrowingRecordException;
import com.library.exception.PatronException;
import com.library.model.BorrowingRecord;
import com.library.service.BorrowingRecordService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Date;

import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class BorrowingRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BorrowingRecordService borrowingRecordService;

    @Test
    @WithMockUser(username="admin", roles={"USER"})
    public void testBorrowBook() throws Exception {
        BorrowingRecord borrowingRecord = new BorrowingRecord(1, null, null, new Date(), null);

        when(borrowingRecordService.borrowBook(1, 1)).thenReturn(borrowingRecord);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/borrow/1/patron/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.borrowDate").exists());
    }

    @Test
    @WithMockUser(username="admin", roles={"USER"})
    public void testBorrowBook_BookNotFound() throws Exception {
        when(borrowingRecordService.borrowBook(1, 1)).thenThrow(new BookException("Book not found"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/borrow/1/patron/1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("Book not found"));
    }

    @Test
    @WithMockUser(username="admin", roles={"USER"})
    public void testBorrowBook_PatronNotFound() throws Exception {
        when(borrowingRecordService.borrowBook(1, 1)).thenThrow(new PatronException("Patron not found"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/borrow/1/patron/1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("Patron not found"));
    }

    @Test
    @WithMockUser(username="admin", roles={"USER"})
    public void testBorrowBook_InvalidBorrowingRecord() throws Exception {
        when(borrowingRecordService.borrowBook(1, 1)).thenThrow(new BorrowingRecordException("Invalid borrowing record"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/borrow/1/patron/1"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Invalid borrowing record"));
    }

    @Test
    @WithMockUser(username="admin", roles={"USER"})
    public void testReturnBook() throws Exception {
        BorrowingRecord borrowingRecord = new BorrowingRecord(1, null, null, new Date(), new Date());

        when(borrowingRecordService.returnBook(1, 1)).thenReturn(borrowingRecord);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/return/1/patron/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.returnDate").exists());
    }

    @Test
    @WithMockUser(username="admin", roles={"USER"})
    public void testReturnBook_BookNotFound() throws Exception {
        when(borrowingRecordService.returnBook(1, 1)).thenThrow(new BookException("Book not found"));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/return/1/patron/1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("Book not found"));
    }

    @Test
    @WithMockUser(username="admin", roles={"USER"})
    public void testReturnBook_PatronNotFound() throws Exception {
        when(borrowingRecordService.returnBook(1, 1)).thenThrow(new PatronException("Patron not found"));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/return/1/patron/1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("Patron not found"));
    }

    @Test
    @WithMockUser(username="admin", roles={"USER"})
    public void testReturnBook_InvalidBorrowingRecord() throws Exception {
        when(borrowingRecordService.returnBook(1, 1)).thenThrow(new BorrowingRecordException("Invalid borrowing record"));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/return/1/patron/1"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Invalid borrowing record"));
    }
}
