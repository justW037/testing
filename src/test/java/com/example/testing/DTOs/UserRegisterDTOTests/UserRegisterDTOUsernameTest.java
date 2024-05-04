package com.example.testing.DTOs.UserRegisterDTOTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.example.testing.DTOs.UserRegisterDTO;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class UserRegisterDTOUsernameTest {
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
    public void testValidUsername() {
        validUserRegisterDTO.setUsername("username");
        assertTrue(validator.validate(validUserRegisterDTO).isEmpty());
        LOGGER.info("Tên người dùng hợp lệ");
    }

    @Test
    public void testUsernameNotBlank() {
        validUserRegisterDTO.setUsername("");
        Set<ConstraintViolation<UserRegisterDTO>> violations = validator.validate(validUserRegisterDTO);
        assertFalse(violations.isEmpty());
        assertEquals("Username không được để trống", violations.iterator().next().getMessage());
        LOGGER.info(violations.iterator().next().getMessage());
    }

    @Test
    public void testUsernameTooLong() {
        validUserRegisterDTO.setUsername("thisistoolongusernam");
        Set<ConstraintViolation<UserRegisterDTO>> violations = validator.validate(validUserRegisterDTO);
        assertFalse(violations.isEmpty());
        assertEquals("Tên tối đa 15 ký tự", violations.iterator().next().getMessage());
        LOGGER.info(violations.iterator().next().getMessage());
    }

    @Test
    public void testValidUsername_AtUpperBoundary() {
        validUserRegisterDTO.setUsername("ABCDEFGHIJKLMNO");
        assertTrue(validator.validate(validUserRegisterDTO).isEmpty());
        LOGGER.info("Tên người dùng hợp lệ");
    }

    @Test
    public void testValidUsername_AtNearUpperBoundary() {
        validUserRegisterDTO.setUsername("ABCDEFGHIJKLMN");
        assertTrue(validator.validate(validUserRegisterDTO).isEmpty());
        LOGGER.info("Tên người dùng hợp lệ");
    }

    @Test
    public void testValidUsername_AtNearLowerBoundary() {
        validUserRegisterDTO.setUsername("A");
        assertTrue(validator.validate(validUserRegisterDTO).isEmpty());
        LOGGER.info("Tên người dùng hợp lệ");
    }
}
