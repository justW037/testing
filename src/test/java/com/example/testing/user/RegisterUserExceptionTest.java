package com.example.testing.user;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.example.testing.DTOs.UserRegisterDTO;
import com.example.testing.controllers.UserController;
import com.example.testing.exceptions.ExistedResourceException;
import com.example.testing.exceptions.PasswordNotMatchException;
import com.example.testing.repositories.UserRepository;
import com.example.testing.services.user.UserService;


@ExtendWith(MockitoExtension.class)
public class RegisterUserExceptionTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Mock
    private Model model;

    @InjectMocks
    private UserController userController;

    @Test
    public void testCreateUserPasswordNotMatch() {
        UserRegisterDTO userDTO = new UserRegisterDTO("username","12345678" , "12345678", "email@example.com");
        userDTO.setRetypePassword("123456789");

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        assertThrows(PasswordNotMatchException.class, () -> userService.createUser(userDTO));

        assertEquals("Password không khớp", assertThrows(PasswordNotMatchException.class, () -> userService.createUser(userDTO)).getMessage());

        assertEquals("register", userController.createUser(userDTO, bindingResult, model));
        verify(model).addAttribute(eq("errorMessages"), any());
    }

    @Test
    public void testCreateUserEmailExists() {
        UserRegisterDTO userDTO = new UserRegisterDTO("username","password" , "password", "email@example.com");
        userDTO.setEmail("existing@example.com");

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        when(userRepository.existsByEmail(userDTO.getEmail())).thenReturn(true);

        assertThrows(ExistedResourceException.class, () -> userService.createUser(userDTO));

        assertEquals("Email đã tồn tại", assertThrows(ExistedResourceException.class, () -> userService.createUser(userDTO)).getMessage());

        assertEquals("register", userController.createUser(userDTO, bindingResult, model));
        verify(model).addAttribute(eq("errorMessages"), any());
    }

    @Test
    public void testCreateUserUsernameExists() throws Exception {
        UserRegisterDTO userDTO = new UserRegisterDTO("username","password" , "password", "email@example.com");
        userDTO.setUsername("existingUsername");

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        when(userRepository.existsByUsername(userDTO.getUsername())).thenReturn(true);

        assertThrows(ExistedResourceException.class, () -> userService.createUser(userDTO));

        assertEquals("Username đã tồn tại", assertThrows(ExistedResourceException.class, () -> userService.createUser(userDTO)).getMessage());

        assertEquals("register", userController.createUser(userDTO, bindingResult, model));
        verify(model).addAttribute(eq("errorMessages"), any());
    }
    
}
