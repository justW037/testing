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


public class UserRegisterDTOPasswordTest {
    private static Validator validator;
    private static UserRegisterDTO validUserRegisterDTO;
    private static final Logger LOGGER = Logger.getLogger(UserRegisterDTOUsernameTest.class.getName());

    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        validUserRegisterDTO = new UserRegisterDTO("username", "password", "password", "email@example.com");
    }

    // @Test
    // public void testPassword_NotBlank() {
    //     validUserRegisterDTO.setPassword("               ");
    //     Set<ConstraintViolation<UserRegisterDTO>> violations = validator.validate(validUserRegisterDTO);
    //     assertFalse(violations.isEmpty());
    //     assertEquals("Password không được để trống", violations.iterator().next().getMessage());
    //     LOGGER.info(violations.iterator().next().getMessage());
    // }

    @Test
    public void testPassword_OutOfLowerBoundary() {
        validUserRegisterDTO.setPassword("passwor");
        Set<ConstraintViolation<UserRegisterDTO>> violations = validator.validate(validUserRegisterDTO);
        assertFalse(violations.isEmpty());
        assertEquals("Password phải có độ dài từ 8 đến 12 ký tự", violations.iterator().next().getMessage());
        LOGGER.info(violations.iterator().next().getMessage());
    }

    @Test
    public void testPassword_OutOfUpperBoundary() {
        validUserRegisterDTO.setPassword("passwordpassr");
        Set<ConstraintViolation<UserRegisterDTO>> violations = validator.validate(validUserRegisterDTO);
        assertFalse(violations.isEmpty());
        assertEquals("Password phải có độ dài từ 8 đến 12 ký tự", violations.iterator().next().getMessage());
        LOGGER.info(violations.iterator().next().getMessage());
    }

    @Test
    public void testPassword_AtUpperBoundary() {
        validUserRegisterDTO.setPassword("passwordpass");
        assertTrue(validator.validate(validUserRegisterDTO).isEmpty());
        LOGGER.info("Mật khẩu hợp lệ");
    }

    @Test
    public void testPassword_AtLowerBoundary() {
        validUserRegisterDTO.setPassword("password");
        assertTrue(validator.validate(validUserRegisterDTO).isEmpty());
        LOGGER.info("Mật khẩu hợp lệ");
    }

    @Test
    public void testPassword_AtNearUpperBoundary() {
        validUserRegisterDTO.setPassword("passwordpas");
        assertTrue(validator.validate(validUserRegisterDTO).isEmpty());
        LOGGER.info("Mật khẩu hợp lệ");
    }

    @Test
    public void testPassword_AtNearLowerBoundary() {
        validUserRegisterDTO.setPassword("passwordp");
        assertTrue(validator.validate(validUserRegisterDTO).isEmpty());
        LOGGER.info("Mật khẩu hợp lệ");
    }

    @Test
    public void testValidPassword() {
        validUserRegisterDTO.setPassword("passwordab");
        assertTrue(validator.validate(validUserRegisterDTO).isEmpty());
        LOGGER.info("Mật khẩu hợp lệ");
    }
}
