package com.example.auth.service.implementation;



import com.fleetguard360.presentation.dto.AuthResDTO;
import com.fleetguard360.service.exception.InvalidCredentialsException;
import com.fleetguard360.service.implementation.AuthServiceImpl;
import com.fleetguard360.util.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para AuthServiceImpl usando el patrón AAA
 */
class AuthServiceImplTest {

    private AuthenticationManager authenticationManager;
    private JwtUtils jwtUtils;
    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        authenticationManager = mock(AuthenticationManager.class);
        jwtUtils = mock(JwtUtils.class);
        authService = new AuthServiceImpl(authenticationManager, jwtUtils);
    }

    @Test
    void shouldReturnAuthResDTOWhenLoginIsSuccessful() {
        // Arrange
        String email = "user@example.com";
        String password = "password123";

        Authentication authentication = mock(Authentication.class);
        UserDetails userDetails = mock(UserDetails.class);

        when(userDetails.getUsername()).thenReturn(email);
        when(userDetails.isEnabled()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        when(jwtUtils.createToken(authentication)).thenReturn("jwt-token-123");

        // Act
        AuthResDTO result = authService.login(email, password);

        // Assert
        assertNotNull(result);
        assertEquals(email, result.email());
        assertEquals("Loggeado", result.message());
        assertEquals("jwt-token-123", result.jwt());
        assertTrue(result.status());

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtils, times(1)).createToken(authentication);
    }

    @Test
    void shouldThrowInvalidCredentialsExceptionWhenBadCredentials() {
        // Arrange
        String email = "invalid@example.com";
        String password = "wrong";
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        // Act & Assert
        InvalidCredentialsException exception = assertThrows(
                InvalidCredentialsException.class,
                () -> authService.login(email, password)
        );

        assertEquals("Bad credentials", exception.getMessage());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtils, never()).createToken(any());
    }
}

