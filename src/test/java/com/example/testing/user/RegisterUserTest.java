package com.example.testing.user;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.MapBindingResult;

import com.example.testing.DTOs.UserRegisterDTO;
import com.example.testing.controllers.UserController;
import com.example.testing.exceptions.ExistedResourceException;
import com.example.testing.exceptions.PasswordNotMatchException;
import com.example.testing.models.User;
import com.example.testing.repositories.UserRepository;
import com.example.testing.services.user.IUserService;
import com.example.testing.services.user.UserService;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@ExtendWith(MockitoExtension.class)
public class RegisterUserTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private IUserService userService;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private Model model;

    @InjectMocks
    private UserController userController;

    @Mock
    private PasswordEncoder passwordEncoder;
    
    @BeforeEach
    public void setUp() {

        MockitoAnnotations.openMocks(this);
        
    }

    public void userRegisterValidationTest(UserRegisterDTO dto, String expectedErrorMessage) throws Exception {
        // Khởi tạo validator
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
    
        if (validator.validate(dto).isEmpty()) {
            assertTrue(validator.validate(dto).isEmpty());

            assertEquals("redirect:login", userController.createUser(dto, bindingResult, model));
            // verify(userService).createUser(userRegisterDTO);
        
        } else {
        List<String> expectedErrorMessages = Arrays.asList(expectedErrorMessage.split(";"));    
        Set<ConstraintViolation<UserRegisterDTO>> violations = validator.validate(dto);
    
        // Tạo một đối tượng BindingResult và thêm các ConstraintViolation vào đó
        BindingResult result = new MapBindingResult(new HashMap<>(), "userRegisterDTO");
        for (ConstraintViolation<UserRegisterDTO> violation : violations) {
            result.addError(new FieldError("userRegisterDTO", violation.getPropertyPath().toString(), violation.getMessage()));
        }
        // Tạo đối tượng Model giả
        Model model = mock(Model.class);
        // Thực hiện test controller với BindingResult đã được gán kết quả validate
        assertEquals("register", userController.createUser(dto, result, model));
            
        List<String> actualErrorMessages = result.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());
        assertEquals(expectedErrorMessages.size(), actualErrorMessages.size());
        Collections.sort(actualErrorMessages);
        Collections.sort(expectedErrorMessages);
        assertArrayEquals(expectedErrorMessages.toArray(), actualErrorMessages.toArray());
        // Kiểm tra xem model đã được thêm attribute "errorMessages" không
        verify(model, times(1)).addAttribute(eq("errorMessages"), anyList());
        }
    }
    
    @ParameterizedTest
    @CsvFileSource(resources = "/test/user/userRegisterTest.csv", numLinesToSkip = 1)
    void testUserRegister(String username, String password, String retypePassword, String email, String expectedErrorMessage) throws Exception {
        UserRegisterDTO dto = UserRegisterDTO.builder()
                .username(username)
                .password(password)
                .retypePassword(retypePassword)
                .email(email)
                .build();
        userRegisterValidationTest(dto, expectedErrorMessage);
    }

}
