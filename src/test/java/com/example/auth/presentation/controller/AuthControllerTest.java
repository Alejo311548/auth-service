package com.example.auth.presentation.controller;



import com.fleetguard360.presentation.controller.AuthController;
import com.fleetguard360.presentation.dto.AuthReqDTO;
import com.fleetguard360.presentation.dto.AuthResDTO;
import com.fleetguard360.service.interfaces.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para AuthController usando el patrón AAA (Arrange - Act - Assert)
 */
class AuthControllerTest {

    private AuthService authService;
    private AuthController authController;

    @BeforeEach
    void setUp() {
        authService = mock(AuthService.class);
        authController = new AuthController(authService);
    }

    @Test
    void shouldReturnAuthResponseWhenLoginIsSuccessful() {
        // Arrange
        AuthReqDTO request = new AuthReqDTO("user@example.com", "password123");
        AuthResDTO expectedResponse = new AuthResDTO(
                "user@example.com",
                "Login exitoso",
                "jwt-token-123",
                true
        );

        when(authService.login("user@example.com", "password123")).thenReturn(expectedResponse);

        // Act
        ResponseEntity<AuthResDTO> response = authController.login(request);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedResponse, response.getBody());
        verify(authService, times(1)).login("user@example.com", "password123");
    }

    @Test
    void shouldPropagateExceptionWhenAuthServiceFails() {
        // Arrange
        AuthReqDTO request = new AuthReqDTO("baduser@example.com", "wrongpass");
        when(authService.login(anyString(), anyString()))
                .thenThrow(new RuntimeException("Credenciales inválidas"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authController.login(request));

        assertEquals("Credenciales inválidas", exception.getMessage());
        verify(authService, times(1)).login("baduser@example.com", "wrongpass");
    }
}

