package com.example.auth.service.implementation;



import com.fleetguard360.persistence.entity.User;
import com.fleetguard360.persistence.repository.UserRepository;
import com.fleetguard360.service.exception.ResourceNotFoundException;
import com.fleetguard360.service.implementation.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("test@example.com");
        mockUser.setPassword("password123");
    }



    @Test
    void getById_ShouldReturnUser_WhenUserExists() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        // Act
        User result = userService.getById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(mockUser.getEmail(), result.getEmail());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void getById_ShouldThrowException_WhenUserDoesNotExist() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> userService.getById(1L)
        );

        assertTrue(exception.getMessage().contains("id: 1"));
        verify(userRepository, times(1)).findById(1L);
    }



    @Test
    void getByEmail_ShouldReturnUser_WhenUserExists() {
        // Arrange
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(mockUser));

        // Act
        User result = userService.getByEmail("test@example.com");

        // Assert
        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        verify(userRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    void getByEmail_ShouldThrowException_WhenUserDoesNotExist() {
        // Arrange
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> userService.getByEmail("notfound@example.com")
        );

        assertTrue(exception.getMessage().contains("email"));
        verify(userRepository, times(1)).findByEmail("notfound@example.com");
    }
}

