package com.example.auth.presentation.dto;




import com.fleetguard360.presentation.dto.AuthResDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para AuthResDTO usando patrón AAA (Arrange – Act – Assert)
 */
class AuthResDTOTest {

    @Test
    void shouldCreateAuthResDTOAndReturnCorrectValues() {
        // Arrange
        String email = "test@example.com";
        String message = "Success";
        String jwt = "token-123";
        boolean status = true;

        // Act
        AuthResDTO dto = new AuthResDTO(email, message, jwt, status);

        // Assert
        assertEquals(email, dto.email());
        assertEquals(message, dto.message());
        assertEquals(jwt, dto.jwt());
        assertTrue(dto.status());
    }

    @Test
    void shouldVerifyEqualityAndToStringMethods() {
        // Arrange
        AuthResDTO dto1 = new AuthResDTO("a@b.com", "OK", "tkn", true);
        AuthResDTO dto2 = new AuthResDTO("a@b.com", "OK", "tkn", true);
        AuthResDTO dto3 = new AuthResDTO("other@b.com", "Fail", "tkn2", false);

        // Act & Assert
        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);
        assertTrue(dto1.toString().contains("a@b.com"));
        assertTrue(dto1.hashCode() == dto2.hashCode());
    }
}

