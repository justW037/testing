package com.example.testing.DTOs.UserRegisterDTOTests;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.example.testing.DTOs.UserRegisterDTO;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class UserRegisterDTOEmailTest {
     private static Validator validator;
    private static UserRegisterDTO validUserRegisterDTO;
    private static final Logger LOGGER = Logger.getLogger(UserRegisterDTOUsernameTest.class.getName());

    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        validUserRegisterDTO = new UserRegisterDTO("username", "password", "password", "email@example.com");
    }

    @Test
    public void testInvalidEmailFormat() {
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO("username", "password", "password", "invalid-email-format");
        assertFalse(validator.validate(userRegisterDTO).isEmpty());
    }

    @Test
    public void testValidEmailFormat() {
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO("username", "password", "password", "valid.email@example.com");
        assertTrue(validator.validate(userRegisterDTO).isEmpty());
    }
    @Test
    public void testMaxEmailLength() {
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO("username", "password", "password", "email@example.com.with.very.long.domain");
        assertFalse(validator.validate(userRegisterDTO).isEmpty());
    }
}
