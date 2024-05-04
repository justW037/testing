package com.example.testing.services;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.testing.DTOs.UserLoginDTO;
import com.example.testing.DTOs.UserRegisterDTO;
import com.example.testing.exceptions.DataNotFoundException;
import com.example.testing.exceptions.ExistedResourceException;
import com.example.testing.exceptions.PasswordNotMatchException;
import com.example.testing.models.Student;
import com.example.testing.models.User;
import com.example.testing.repositories.UserRepository;
import com.example.testing.services.user.UserService;

public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateUser_Success() throws Exception {

        UserRegisterDTO userDTO = new UserRegisterDTO("username","password" , "password", "email@example.com");

        when(userRepository.existsByEmail(userDTO.getEmail())).thenReturn(false);

        when(userRepository.existsByUsername(userDTO.getUsername())).thenReturn(false);

        when(passwordEncoder.encode(userDTO.getPassword())).thenReturn("encodedPassword");
        
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L); 
            return user;
        });

        User createdUser = userService.createUser(userDTO);

        assertNotNull(createdUser);
        assertEquals("username", createdUser.getUsername());
        assertEquals("email@example.com", createdUser.getEmail());
        assertEquals("encodedPassword", createdUser.getPassword());

    }

    @Test
    public void testCreateUser_PasswordNotMatch_ReturnsException() {
        // Arrange
        UserRegisterDTO userDTO = new UserRegisterDTO();
        userDTO.setPassword("password1");
        userDTO.setRetypePassword("password2");

        assertThrows(PasswordNotMatchException.class, () -> userService.createUser(userDTO), "Password không khớp");
    }

    @Test
    public void testCreateUser_EmailExists_ReturnsException() {
        UserRegisterDTO userDTO = new UserRegisterDTO();
        userDTO.setEmail("existing@example.com");
        userDTO.setPassword("password");
        userDTO.setRetypePassword("password");
        when(userRepository.existsByEmail(userDTO.getEmail())).thenReturn(true);

        assertThrows(ExistedResourceException.class, () -> userService.createUser(userDTO), "Email đã tồn tại");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testCreateUser_UsernameExists_ReturnsException() {
        // Arrange
        UserRegisterDTO userDTO = new UserRegisterDTO();
        userDTO.setUsername("existingUser");
        userDTO.setPassword("password");
        userDTO.setRetypePassword("password");
        when(userRepository.existsByUsername(userDTO.getUsername())).thenReturn(true);

        assertThrows(ExistedResourceException.class, () -> userService.createUser(userDTO), "Username đã tồn tại");
    }

    @Test
    public void testLogin_Success() throws Exception {

        String username = "username";
        String password = "password";
        String encodedPassword = "encodedPassword";

        UserLoginDTO userDTO = new UserLoginDTO(username, password);

        User user = new User(1L, username, encodedPassword,"email@example.com");

        when(userRepository.findByUsername(username)).thenReturn(java.util.Optional.of(user));
        
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(true);

        User loggedInUser = userService.login(userDTO);

        assertNotNull(loggedInUser);
        assertEquals(username, loggedInUser.getUsername());
        assertEquals("email@example.com", loggedInUser.getEmail());
    }

    @Test
    public void testLogin_UserNotFound_ReturnsException() {
        // Arrange
        UserLoginDTO userDTO = new UserLoginDTO();
        userDTO.setUsername("nonexistentUser");
        when(userRepository.findByUsername(userDTO.getUsername())).thenReturn(java.util.Optional.empty());

        // Act & Assert
        assertThrows(DataNotFoundException.class, () -> userService.login(userDTO), "Người dùng không tồn tại");
    }

    @Test
    public void testLogin_IncorrectPassword_ReturnsException() {
        // Arrange
        UserLoginDTO userDTO = new UserLoginDTO();
        userDTO.setUsername("existingUser");
        userDTO.setPassword("incorrectPassword");
        User existingUser = new User();
        existingUser.setUsername("existingUser");
        existingUser.setPassword("correctPassword"); 

        when(userRepository.findByUsername(userDTO.getUsername())).thenReturn(java.util.Optional.of(existingUser));
        when(passwordEncoder.matches(userDTO.getPassword(), existingUser.getPassword())).thenReturn(false);

        assertThrows(PasswordNotMatchException.class, () -> userService.login(userDTO), "Mật khẩu không chính xác");
    }
}
