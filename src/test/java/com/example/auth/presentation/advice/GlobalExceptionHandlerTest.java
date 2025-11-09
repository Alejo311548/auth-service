package com.example.auth.presentation.advice;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fleetguard360.presentation.advice.GlobalExceptionHandler;
import com.fleetguard360.presentation.dto.ApiErrorResDTO;
import com.fleetguard360.service.exception.InvalidCredentialsException;
import com.fleetguard360.service.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
        request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/api/test");
    }

    @Test
    void handleInvalidCredentials_ShouldReturnUnauthorized() {
        // Arrange
        InvalidCredentialsException ex = new InvalidCredentialsException("Invalid credentials");

        // Act
        ResponseEntity<ApiErrorResDTO> response = handler.handleInvalidCredentials(ex, request);

        // Assert
        assertEquals(401, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Invalid credentials", response.getBody().message());
        assertEquals("/api/test", response.getBody().path());
        assertEquals(response.getStatusCode(), response.getBody().status());
        assertTrue(response.getBody().timestamp().isBefore(Instant.now()));
    }

    @Test
    void handleResourceNotFound_ShouldReturnNotFound() {
        // Arrange
        ResourceNotFoundException ex = new ResourceNotFoundException("Resource not found");

        // Act
        ResponseEntity<ApiErrorResDTO> response = handler.handleResourceNotFound(ex, request);

        // Assert
        assertEquals(404, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Resource not found", response.getBody().message());
    }

    @Test
    void handlerJWTVerification_ShouldReturnUnauthorized() {
        // Arrange
        JWTVerificationException ex = new JWTVerificationException("Invalid token");

        // Act
        ResponseEntity<ApiErrorResDTO> response = handler.handlerJWTVerification(ex, request);

        // Assert
        assertEquals(401, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Invalid token", response.getBody().message());
    }
}

