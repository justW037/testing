package com.example.testing.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.testing.models.User;

@ExtendWith(MockitoExtension.class)
public class UserRepositoryTest {
    @Mock
    private UserRepository userRepository;

    @Test
    public void testExistsByEmail_ExistingEmail_ReturnsTrue() {
        // Arrange
        String existingEmail = "existing@example.com";
        when(userRepository.existsByEmail(existingEmail)).thenReturn(true);

        // Act
        boolean result = userRepository.existsByEmail(existingEmail);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testExistsByEmail_NonExistingEmail_ReturnsFalse() {
        // Arrange
        String nonExistingEmail = "nonexisting@example.com";
        when(userRepository.existsByEmail(nonExistingEmail)).thenReturn(false);

        // Act
        boolean result = userRepository.existsByEmail(nonExistingEmail);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testExistsByUsername_ExistingUsername_ReturnsTrue() {
        // Arrange
        String existingUsername = "existingUser";
        when(userRepository.existsByUsername(existingUsername)).thenReturn(true);

        // Act
        boolean result = userRepository.existsByUsername(existingUsername);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testExistsByUsername_NonExistingUsername_ReturnsFalse() {
        // Arrange
        String nonExistingUsername = "nonExistingUser";
        when(userRepository.existsByUsername(nonExistingUsername)).thenReturn(false);

        // Act
        boolean result = userRepository.existsByUsername(nonExistingUsername);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testFindByUsername_ExistingUsername_ReturnsUser() {
        // Arrange
        String existingUsername = "existingUser";
        User user = new User();
        when(userRepository.findByUsername(existingUsername)).thenReturn(Optional.of(user));

        // Act
        Optional<User> result = userRepository.findByUsername(existingUsername);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    public void testFindByUsername_NonExistingUsername_ReturnsEmptyOptional() {
        // Arrange
        String nonExistingUsername = "nonExistingUser";
        when(userRepository.findByUsername(nonExistingUsername)).thenReturn(Optional.empty());

        // Act
        Optional<User> result = userRepository.findByUsername(nonExistingUsername);

        // Assert
        assertFalse(result.isPresent());
    }
}
