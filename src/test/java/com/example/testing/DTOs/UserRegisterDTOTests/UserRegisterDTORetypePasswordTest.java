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

public class UserRegisterDTORetypePasswordTest {
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
    // public void testRetypePassword_NotBlank() {
    //     validUserRegisterDTO.setRetypePassword("");
    //     Set<ConstraintViolation<UserRegisterDTO>> violations = validator.validate(validUserRegisterDTO);
    //     assertFalse(violations.isEmpty());
    //     assertEquals("Password xác nhận không được để trống", violations.iterator().next().getMessage());
    //     LOGGER.info(violations.iterator().next().getMessage());
    // }

    @Test
    public void testRetypePassword_OutOfLowerBoundary() {
        validUserRegisterDTO.setRetypePassword("passwor");
        Set<ConstraintViolation<UserRegisterDTO>> violations = validator.validate(validUserRegisterDTO);
        assertFalse(violations.isEmpty());
        assertEquals("Password xác nhận phải có độ dài từ 8 đến 12 ký tự", violations.iterator().next().getMessage());
        LOGGER.info(violations.iterator().next().getMessage());
    }

    @Test
    public void testRetypePassword_OutOfUpperBoundary() {
        validUserRegisterDTO.setRetypePassword("passwordpassr");
        Set<ConstraintViolation<UserRegisterDTO>> violations = validator.validate(validUserRegisterDTO);
        assertFalse(violations.isEmpty());
        assertEquals("Password xác nhận phải có độ dài từ 8 đến 12 ký tự", violations.iterator().next().getMessage());
        LOGGER.info(violations.iterator().next().getMessage());
    }

    @Test
    public void testRetypePassword_AtUpperBoundary() {
        validUserRegisterDTO.setRetypePassword("passwordpass");
        assertTrue(validator.validate(validUserRegisterDTO).isEmpty());
        LOGGER.info("Mật khẩu xác nhận hợp lệ");
    }

    @Test
    public void testRetypePassword_AtLowerBoundary() {
        validUserRegisterDTO.setRetypePassword("password");
        assertTrue(validator.validate(validUserRegisterDTO).isEmpty());
        LOGGER.info("Mật khẩu xác nhận hợp lệ");
    }

    @Test
    public void testRetypePassword_AtNearUpperBoundary() {
        validUserRegisterDTO.setRetypePassword("passwordpas");
        assertTrue(validator.validate(validUserRegisterDTO).isEmpty());
        LOGGER.info("Mật khẩu xác nhận hợp lệ");
    }

    @Test
    public void testRetypePassword_AtNearLowerBoundary() {
        validUserRegisterDTO.setRetypePassword("passwordp");
        assertTrue(validator.validate(validUserRegisterDTO).isEmpty());
        LOGGER.info("Mật khẩu xác nhận hợp lệ");
    }

    @Test
    public void testValidRetypePassword() {
        validUserRegisterDTO.setRetypePassword("passwordab");
        assertTrue(validator.validate(validUserRegisterDTO).isEmpty());
        LOGGER.info("Mật khẩu xác nhận hợp lệ");
    }

}
