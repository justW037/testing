package com.example.testing.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.example.testing.DTOs.UserLoginDTO;
import com.example.testing.DTOs.UserRegisterDTO;
import com.example.testing.controllers.UserController;
import com.example.testing.exceptions.DataNotFoundException;
import com.example.testing.exceptions.ExistedResourceException;
import com.example.testing.exceptions.PasswordNotMatchException;
import com.example.testing.models.User;
import com.example.testing.repositories.UserRepository;
import com.example.testing.services.user.UserService;

import jakarta.servlet.http.HttpSession;


@ExtendWith(MockitoExtension.class)
public class LoginUserExceptionTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Mock
    private BindingResult bindingResult;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    HttpSession session;

    @Mock
    private Model model;

    @InjectMocks
    private UserController userController;

    @Test
    public void testLoginUserNotFoundException() {
        UserLoginDTO userDTO = new UserLoginDTO("nonexistentUser", "123456789");

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        when(userRepository.findByUsername(userDTO.getUsername())).thenReturn(Optional.empty());

        assertEquals("Người dùng không tồn tại", assertThrows(DataNotFoundException.class, () -> userService.login(userDTO)).getMessage());

        assertEquals("login", userController.login(userDTO, bindingResult , model, session));
        verify(model).addAttribute(eq("errorMessages"), any());
    }

    @Test
    public void testLoginPasswordNotMatchException() {
        UserLoginDTO userDTO = new UserLoginDTO("username", "incorrectPassword");
        User existingUser = new User();
        existingUser.setUsername("existingUser");
        existingUser.setPassword("correctPassword"); 

        when(userRepository.findByUsername(userDTO.getUsername())).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches(userDTO.getPassword(), existingUser.getPassword())).thenReturn(false);

        assertEquals("Mật khẩu không chính xác", assertThrows(PasswordNotMatchException.class, () -> userService.login(userDTO)).getMessage());

        assertEquals("login", userController.login(userDTO, bindingResult , model, session));

        verify(model).addAttribute(eq("errorMessages"), any());
    }

}
