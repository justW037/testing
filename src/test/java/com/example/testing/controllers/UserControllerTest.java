package com.example.testing.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.example.testing.DTOs.UserLoginDTO;
import com.example.testing.DTOs.UserRegisterDTO;
import com.example.testing.models.User;
import com.example.testing.services.user.IUserService;

import jakarta.servlet.http.HttpSession;

public class UserControllerTest {

    @Mock
    private IUserService userService;

    @Mock
    private Model model;

    @InjectMocks
    private UserController userController;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private HttpSession session;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testShowLoginForm() {

        // Act
        String viewName = userController.showLoginForm(model);

        // Assert
        assertEquals("login", viewName);
        verify(model).addAttribute("userLoginDTO", new UserLoginDTO());
    }

    @Test
    public void testShowRegistrationForm() {
        // Act
        String viewName = userController.showRegistrationForm(model);

        // Assert
        assertEquals("register", viewName);
        verify(model).addAttribute("userRegisterDTO", new UserRegisterDTO());
    }

    @Test
    public void testCreateUser_WithErrors() {
        // Arrange
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(new FieldError("userRegisterDTO", "password", "Password không hợp lệ")));

        // Act
        String viewName = userController.createUser(userRegisterDTO, bindingResult, model);

        // Assert
        assertEquals("register", viewName);
        verify(model).addAttribute(eq("errorMessages"), any());
    }

    @Test
    public void testCreateUser_Success() throws Exception {
        // Arrange
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        when(bindingResult.hasErrors()).thenReturn(false);

        // Act
        String viewName = userController.createUser(userRegisterDTO, bindingResult, model);

        // Assert
        assertEquals("redirect:login", viewName);
        verify(userService).createUser(userRegisterDTO);
    }

    @Test
    public void testCreateUser_ExceptionThrown() throws Exception {
        // Arrange
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        when(bindingResult.hasErrors()).thenReturn(false);
        when(userService.createUser(userRegisterDTO)).thenThrow(new Exception("Error message"));

        // Act
        String viewName = userController.createUser(userRegisterDTO, bindingResult, model);

        // Assert
        assertEquals("register", viewName);
        verify(model).addAttribute(eq("errorMessages"), any());
    }

        @Test
    public void testLogin_SuccessfulLogin_RedirectToHomePage() throws Exception {
        // Arrange
        UserLoginDTO userLoginDTO = new UserLoginDTO("username", "password");
        User user = new User();
        when(userService.login(userLoginDTO)).thenReturn(user);

        // Act
        String result = userController.login(userLoginDTO, mock(BindingResult.class), model, session);

        // Assert
        verify(session).setAttribute("user", user);
        assertEquals("redirect:/students/home", result);
    }

    @Test
    public void testLogin_InvalidUser_ReturnLoginPageWithError() throws Exception {
        UserLoginDTO userLoginDTO = new UserLoginDTO("username", "password");
    when(userService.login(userLoginDTO)).thenThrow(new Exception("Người dùng không tồn tại"));

    // Act
    String viewName = userController.login(userLoginDTO, bindingResult, model, session);

    // Assert
    verify(bindingResult).hasErrors();
    verify(model).addAttribute("errorMessages", "Người dùng không tồn tại");
    assertEquals("login", viewName);
    }

    @Test
    public void testLogin_ValidationErrors_ReturnsLoginPageWithError() throws Exception {
        // Arrange
        UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setUsername(""); // Set an empty username to trigger validation error
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(new FieldError("userLoginDTO", "username", "Username không được để trống")));

        // Act
        String viewName = userController.login(userLoginDTO, bindingResult, model, session);

        // Assert
        verify(bindingResult).hasErrors();
        verify(bindingResult).getFieldErrors();
        verify(model).addAttribute("errorMessages", Collections.singletonList("Username không được để trống"));
        assertEquals("login", viewName);
    }

}