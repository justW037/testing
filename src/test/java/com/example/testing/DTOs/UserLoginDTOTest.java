package com.example.testing.DTOs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class UserLoginDTOTest {
    private static Validator validator;

    @BeforeAll
    public static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidUsernameAndPassword() {
        UserLoginDTO userLoginDTO = new UserLoginDTO("username", "password");
        assertTrue(validator.validate(userLoginDTO).isEmpty());
    }

    @Test
    public void testInvalidUsername_Empty() {
        UserLoginDTO userLoginDTO = new UserLoginDTO("", "password");
        assertFalse(validator.validate(userLoginDTO).isEmpty());
    }

    @Test
    public void testInvalidUsername_TooLong() {
        UserLoginDTO userLoginDTO = new UserLoginDTO("thisUsernameIsTooLong", "password");
        assertFalse(validator.validate(userLoginDTO).isEmpty());
    }

    @Test
    public void testInvalidPassword_Empty() {
        UserLoginDTO userLoginDTO = new UserLoginDTO("validUsername", "");
        assertFalse(validator.validate(userLoginDTO).isEmpty());
    }

    @Test
    public void testInvalidPassword_TooShort() {
        UserLoginDTO userLoginDTO = new UserLoginDTO("validUsername", "short");
        assertFalse(validator.validate(userLoginDTO).isEmpty());
    }

    @Test
    public void testInvalidPassword_TooLong() {
        UserLoginDTO userLoginDTO = new UserLoginDTO("validUsername", "thisPasswordIsTooLong");
        assertFalse(validator.validate(userLoginDTO).isEmpty());
    }

    @Test
    public void testBuilderAnnotation() {
        UserLoginDTO userRegisterDTO = UserLoginDTO.builder().username("username")
                .password("password")
                .build();
        assertNotNull(userRegisterDTO);
    }

    @Test
    public void testGetterAndSetter() {
        // Arrange
        String username = "testuser";
        String password = "testpassword";
        // Act
        UserLoginDTO userDTO = new UserLoginDTO();
        userDTO.setUsername(username);
        userDTO.setPassword(password);

        // Assert
        assertEquals(username, userDTO.getUsername());
        assertEquals(password, userDTO.getPassword());
    }

    @Test
    public void testEqualsAndHashCode() {
        // Arrange
        UserLoginDTO userDTO1 = new UserLoginDTO();
        userDTO1.setUsername("user1");
        userDTO1.setPassword("password1");


        UserLoginDTO userDTO2 = new UserLoginDTO();
        userDTO2.setUsername("user1");
        userDTO2.setPassword("password1");

        // Act & Assert
        assertEquals(userDTO1, userDTO2);
        assertEquals(userDTO1.hashCode(), userDTO2.hashCode());
    }

    @Test
    public void testToString() {
        // Arrange
        UserLoginDTO userDTO = new UserLoginDTO();
        userDTO.setUsername("testuser");
        userDTO.setPassword("testpassword");

        // Act & Assert
        assertEquals("UserLoginDTO(username=testuser, password=testpassword)", userDTO.toString());
    }
}
