package com.example.testing.DTOs.UserRegisterDTOTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;



import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.example.testing.DTOs.UserRegisterDTO;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class UserRegisterDTOTest {
    private static Validator validator;

    @BeforeAll
    public static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

   
    @Test
    public void testEmailNotBlank() {
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO("username", "password", "password", "");
        assertFalse(validator.validate(userRegisterDTO).isEmpty());
    }

    @Test
    public void testEmailFormat() {
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO("username", "password", "password", "invalid-email");
        assertFalse(validator.validate(userRegisterDTO).isEmpty());
    }



  
    @Test
    public void testValidUserRegisterDTO() {
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO("username", "password", "password", "email@example.com");
        assertTrue(validator.validate(userRegisterDTO).isEmpty());
    }



    @Test
    public void testBuilderAnnotation() {
        UserRegisterDTO userRegisterDTO = UserRegisterDTO.builder().username("username")
                .password("password")
                .retypePassword("password")
                .email("email@example.com")
                .build();
        assertNotNull(userRegisterDTO);
    }

    @Test
    public void testGetterAndSetter() {
        // Arrange
        String username = "testuser";
        String password = "testpassword";
        String retypePassword = "testpassword";
        String email = "test@example.com";

        // Act
        UserRegisterDTO userDTO = new UserRegisterDTO();
        userDTO.setUsername(username);
        userDTO.setPassword(password);
        userDTO.setRetypePassword(retypePassword);
        userDTO.setEmail(email);

        // Assert
        assertEquals(username, userDTO.getUsername());
        assertEquals(password, userDTO.getPassword());
        assertEquals(retypePassword, userDTO.getRetypePassword());
        assertEquals(email, userDTO.getEmail());
    }

    @Test
    public void testEqualsAndHashCode() {
        // Arrange
        UserRegisterDTO userDTO1 = new UserRegisterDTO();
        userDTO1.setUsername("user1");
        userDTO1.setPassword("password1");
        userDTO1.setRetypePassword("password1");
        userDTO1.setEmail("email1@example.com");

        UserRegisterDTO userDTO2 = new UserRegisterDTO();
        userDTO2.setUsername("user1");
        userDTO2.setPassword("password1");
        userDTO2.setRetypePassword("password1");
        userDTO2.setEmail("email1@example.com");

        // Act & Assert
        assertEquals(userDTO1, userDTO2);
        assertEquals(userDTO1.hashCode(), userDTO2.hashCode());
    }

    @Test
    public void testToString() {
        // Arrange
        UserRegisterDTO userDTO = new UserRegisterDTO();
        userDTO.setUsername("testuser");
        userDTO.setPassword("testpassword");
        userDTO.setRetypePassword("testpassword");
        userDTO.setEmail("test@example.com");

        // Act & Assert
        assertEquals("UserRegisterDTO(username=testuser, password=testpassword, retypePassword=testpassword, email=test@example.com)", userDTO.toString());
    }
}
