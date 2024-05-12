package com.library.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import com.library.model.Patron;
import com.library.security.PatronDetails;
import com.library.service.PatronLoginService;

@SpringBootTest
@AutoConfigureMockMvc
public class PatronLoginControllerTest {

    @MockBean
    private PatronLoginService patronLoginService;

    @MockBean
    private AuthenticationManager authenticationManager;


        @Test
    public void testRegisterAndLogin() {
        // Mock Patron, PatronLoginService, AuthenticationManager
        Patron patron = new Patron();
        patron.setEmail("test@example.com");
        patron.setPassword("password");

        PatronLoginService patronLoginService = mock(PatronLoginService.class);
        AuthenticationManager authenticationManager = mock(AuthenticationManager.class);

        // Mock UserDetails
        UserDetails userDetails = new User("test@example.com", "password", new ArrayList<>());
        PatronDetails patronDetails = new PatronDetails(patron);

        // Stubbing the login method of PatronLoginService
        when(patronLoginService.login("test@example.com", "password")).thenReturn(userDetails);

        // Stubbing the authenticate method of AuthenticationManager
        Authentication auth = new UsernamePasswordAuthenticationToken(patronDetails, null);
        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("test@example.com", "password")))
                .thenReturn(auth);

        // Set the authentication object to SecurityContextHolder
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Set up controller with mocked dependencies
        PatronLoginController patronLoginController = new PatronLoginController(patronLoginService, authenticationManager);

        // Perform login
        ResponseEntity<?> responseEntity = patronLoginController.login(patron);

        // Assert the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        // You might need to modify this assertion based on the actual behavior of your UserDetails implementation
        assertNotNull(responseEntity.getBody());
    }
}