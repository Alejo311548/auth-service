package com.example.auth.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fleetguard360.util.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para JwtUtils utilizando patrón AAA (Arrange - Act - Assert)
 */
class JwtUtilsTest {

    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();

        ReflectionTestUtils.setField(jwtUtils, "jwtKey", "test-secret-key");
        ReflectionTestUtils.setField(jwtUtils, "jwtUser", "test-issuer");
        ReflectionTestUtils.setField(jwtUtils, "jwtExpire", 3600000);
    }

    @Test
    void shouldCreateValidTokenWithExpectedClaims() {
        // Arrange
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("juan@example.com");

        GrantedAuthority authority = mock(GrantedAuthority.class);
        when(authority.getAuthority()).thenReturn("ROLE_USER");

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(authentication.getAuthorities()).thenReturn((Collection) List.of(authority));

        // Act
        String token = jwtUtils.createToken(authentication);

        // Assert
        assertNotNull(token);
        DecodedJWT decoded = JWT.require(Algorithm.HMAC256("test-secret-key"))
                .withIssuer("test-issuer")
                .build()
                .verify(token);

        assertEquals("juan@example.com", decoded.getSubject());
        assertEquals("ROLE_USER", decoded.getClaim("authorities").asString());
        assertNotNull(decoded.getId());
        assertTrue(decoded.getExpiresAt().after(new Date()));
    }

    @Test
    void shouldFailVerificationWithWrongKey() {
        // Arrange
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("juan@example.com");

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(authentication.getAuthorities()).thenReturn(List.of());

        String token = jwtUtils.createToken(authentication);

        // Act & Assert
        assertThrows(com.auth0.jwt.exceptions.SignatureVerificationException.class, () -> {
            JWT.require(Algorithm.HMAC256("wrong-key"))
                    .withIssuer("test-issuer")
                    .build()
                    .verify(token);
        });
    }
}
