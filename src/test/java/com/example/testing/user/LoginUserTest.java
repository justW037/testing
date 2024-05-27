package com.example.testing.user;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.MapBindingResult;

import com.example.testing.DTOs.UserLoginDTO;
import com.example.testing.DTOs.UserRegisterDTO;
import com.example.testing.controllers.UserController;
import com.example.testing.repositories.UserRepository;
import com.example.testing.services.user.IUserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@ExtendWith(MockitoExtension.class)
public class LoginUserTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private IUserService userService;

    @Mock
    private BindingResult bindingResult;

    
    @Mock
    private Model model;
    
    @Mock
    HttpSession session;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    public void userLoginValidationTest(UserLoginDTO dto, String expectedErrorMessage) throws Exception {
        // Khởi tạo validator
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
    
        if (validator.validate(dto).isEmpty()) {
            assertTrue(validator.validate(dto).isEmpty());

            assertEquals("redirect:/students/home", userController.login(dto, bindingResult, model, session));
            // verify(userService).createUser(userRegisterDTO);
        
        } else {
        List<String> expectedErrorMessages = Arrays.asList(expectedErrorMessage.split(";"));    
        Set<ConstraintViolation<UserLoginDTO>> violations = validator.validate(dto);
    
        // Tạo một đối tượng BindingResult và thêm các ConstraintViolation vào đó
        BindingResult result = new MapBindingResult(new HashMap<>(), "userRegisterDTO");
        for (ConstraintViolation<UserLoginDTO> violation : violations) {
            result.addError(new FieldError("userLoginDTO", violation.getPropertyPath().toString(), violation.getMessage()));
        }
        // Tạo đối tượng Model giả
        Model model = mock(Model.class);
        // Thực hiện test controller với BindingResult đã được gán kết quả validate
        assertEquals("login", userController.login(dto, result, model, session));
            
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
    @CsvFileSource(resources = "/test/user/userLoginTest.csv", numLinesToSkip = 1)
    void testUserRegister(String username, String password, String expectedErrorMessage) throws Exception {
        UserLoginDTO dto = new UserLoginDTO(username, password);
            userLoginValidationTest(dto, expectedErrorMessage);
    }

}
