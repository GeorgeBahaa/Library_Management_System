package com.library.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.exception.PatronException;
import com.library.model.Patron;
import com.library.service.PatronService;
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
public class PatronControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatronService patronService;

    @Test
    public void testGetAllPatrons() throws Exception {
        List<Patron> patrons = new ArrayList<>();
        patrons.add(new Patron(1, "Patron 1", "Address 1", "123456789", "patron1@example.com", "password"));
        patrons.add(new Patron(2, "Patron 2", "Address 2", "987654321", "patron2@example.com", "password"));

        when(patronService.getAllPatrons()).thenReturn(patrons);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/patrons"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Patron 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].address").value("Address 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].phoneNumber").value("123456789"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email").value("patron1@example.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Patron 2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].address").value("Address 2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].phoneNumber").value("987654321"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].email").value("patron2@example.com"));
    }

    @Test
    public void testGetPatronById() throws Exception {
        Patron patron = new Patron(1, "Patron 1", "Address 1", "123456789", "patron1@example.com", "password");

        when(patronService.getPatronById(1)).thenReturn(patron);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/patrons/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Patron 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address").value("Address 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").value("123456789"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("patron1@example.com"));
    }

    @Test
    public void testGetPatronById_PatronNotFound() throws Exception {
        when(patronService.getPatronById(1)).thenThrow(new PatronException("Patron not found"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/patrons/1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("Patron not found"));
    }

    @Test
    public void testAddPatron() throws Exception {
        Patron patron = new Patron(1, "Patron 1", "Address 1", "123456789", "patron1@example.com", "password");

        when(patronService.addPatron(any(Patron.class))).thenReturn(patron);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/patrons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(patron)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Patron 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address").value("Address 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").value("123456789"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("patron1@example.com"));
    }

    @Test
    public void testUpdatePatron() throws Exception {
        Patron patron = new Patron(1, "Patron 1", "Address 1", "123456789", "patron1@example.com", "password");
        Patron updatedPatron = new Patron(1, "Updated Patron", "Updated Address", "987654321", "updatedpatron@example.com", "updatedpassword");

        when(patronService.updatePatron(1, patron)).thenReturn(updatedPatron);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/patrons/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(patron)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testDeletePatron() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/patrons/1"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
