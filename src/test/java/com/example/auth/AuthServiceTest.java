package com.example.auth;




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

class AuthServiceTest {

    private AuthenticationManager authenticationManager;
    private JwtUtils jwtUtils;
    private AuthServiceImpl authService;

    @BeforeEach
    void setup() {
        authenticationManager = mock(AuthenticationManager.class);
        jwtUtils = mock(JwtUtils.class);
        authService = new AuthServiceImpl(authenticationManager, jwtUtils);
    }

    @Test
    void shouldReturnTokenWhenCredentialsAreValid() {
        String email = "juan@example.com";
        String password = "1234";

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn(email);
        when(userDetails.isEnabled()).thenReturn(true);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtUtils.createToken(authentication)).thenReturn("token-xyz");

        AuthResDTO result = authService.login(email, password);

        assertEquals("token-xyz", result.jwt());
        assertEquals(email, result.email());
        verify(authenticationManager, times(1)).authenticate(any());
        verify(jwtUtils, times(1)).createToken(authentication);
    }

    @Test
    void shouldThrowWhenCredentialsAreInvalid() {
        String email = "juan@example.com";
        String password = "wrongpass";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(BadCredentialsException.class);

        assertThrows(InvalidCredentialsException.class,
                () -> authService.login(email, password));

        verify(authenticationManager, times(1)).authenticate(any());
        verifyNoInteractions(jwtUtils);
    }


}



