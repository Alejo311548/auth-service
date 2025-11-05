package com.example.auth.presentation.dto;



import com.fleetguard360.presentation.dto.AuthReqDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para AuthReqDTO siguiendo el patrón AAA (Arrange – Act – Assert)
 */
class AuthReqDTOTest {

    @Test
    void shouldCreateAuthReqDTOAndReturnCorrectValues() {
        // Arrange
        String email = "user@test.com";
        String password = "secret123";

        // Act
        AuthReqDTO dto = new AuthReqDTO(email, password);

        // Assert
        assertEquals(email, dto.email());
        assertEquals(password, dto.password());
    }

    @Test
    void shouldVerifyEqualityAndToStringMethods() {
        // Arrange
        AuthReqDTO dto1 = new AuthReqDTO("user@test.com", "secret123");
        AuthReqDTO dto2 = new AuthReqDTO("user@test.com", "secret123");
        AuthReqDTO dto3 = new AuthReqDTO("other@test.com", "pass456");

        // Act & Assert
        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);
        assertTrue(dto1.toString().contains("user@test.com"));
        assertTrue(dto1.hashCode() == dto2.hashCode());
    }
}

