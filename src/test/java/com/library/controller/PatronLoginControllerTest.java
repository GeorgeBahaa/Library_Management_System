package com.library.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.model.Patron;
import com.library.service.PatronLoginService;

@SpringBootTest
@AutoConfigureMockMvc
public class PatronLoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatronLoginService patronLoginService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testRegisterPatron() throws Exception {
        Patron patron = new Patron();
        patron.setEmail("test@test.com");
        patron.setPassword("password");

        when(patronLoginService.registerPatron(any(Patron.class))).thenReturn(patron);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/patrons/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patron)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(patron.getEmail()));
    }
}