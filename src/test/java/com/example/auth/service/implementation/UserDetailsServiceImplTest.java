package com.example.auth.service.implementation;

import com.fleetguard360.persistence.entity.User;
import com.fleetguard360.persistence.entity.Role;
import com.fleetguard360.persistence.entity.Permission;
import com.fleetguard360.persistence.entity.enums.RoleEnum;
import com.fleetguard360.persistence.entity.enums.PermissionEnum;
import com.fleetguard360.persistence.repository.UserRepository;
import com.fleetguard360.service.exception.ResourceNotFoundException;
import com.fleetguard360.service.implementation.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserDetailsServiceImplTest {


    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Arrange: simular usuario con roles y permisos
        Permission permission = new Permission();
        permission.setPermission(PermissionEnum.READ);

        Role role = new Role();
        role.setRole(RoleEnum.ADMINISTRADOR);
        role.setPermissions(Set.of(permission));

        user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setDisabled(false);
        user.setAccountExpired(false);
        user.setCredentialExpired(false);
        user.setAccountLocked(false);
        user.setRoles(Set.of(role));
    }

    @Test
    void shouldLoadUserByUsernameSuccessfully() {
        // Arrange
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        // Act
        UserDetails userDetails = userDetailsService.loadUserByUsername("test@example.com");

        // Assert
        assertNotNull(userDetails);
        assertEquals("test@example.com", userDetails.getUsername());
        assertEquals("password123", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMINISTRADOR")));
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("READ")));

        verify(userRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        // Arrange
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("notfound@example.com"));

        verify(userRepository, times(1)).findByEmail("notfound@example.com");
    }


}
