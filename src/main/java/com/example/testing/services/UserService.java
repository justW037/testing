package com.example.testing.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.testing.DTOs.UserDTO;
import com.example.testing.models.User;
import com.example.testing.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService implements IUserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public User createUser(UserDTO userDTO) throws Exception{
        if(!userDTO.getPassword().equals(userDTO.getRetypePassword())) {
            throw new Exception("Password không khớp");
        }
        String email = userDTO.getEmail();
        String username = userDTO.getUsername();

        if(userRepository.existsByEmail(email)) {
            throw new Exception("Email đã tồn tại");
        }
        if(userRepository.existsByUsername(username)) {
            throw new Exception("Username đã tồn tại");
        }
        User newUser = User.builder()
                    .username(userDTO.getUsername())
                    .email(userDTO.getEmail())
                    .build();

        String password = userDTO.getPassword();
        String encodedPassword = passwordEncoder.encode(password);
        newUser.setPassword(encodedPassword);

        return userRepository.save(newUser);
    }
}
