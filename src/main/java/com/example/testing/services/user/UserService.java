package com.example.testing.services.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.testing.DTOs.UserLoginDTO;
import com.example.testing.DTOs.UserRegisterDTO;
import com.example.testing.exceptions.DataNotFoundException;
import com.example.testing.exceptions.ExistedResourceException;
import com.example.testing.exceptions.PasswordNotMatchException;
import com.example.testing.models.User;
import com.example.testing.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService implements IUserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public User createUser(UserRegisterDTO userDTO) throws Exception{
        if(!userDTO.getPassword().equals(userDTO.getRetypePassword())) {
            throw new PasswordNotMatchException("Password không khớp");
        }
        String email = userDTO.getEmail();
        String username = userDTO.getUsername();

        if(userRepository.existsByEmail(email)) {
            throw new ExistedResourceException("Email đã tồn tại");
        }
        if(userRepository.existsByUsername(username)) {
            throw new ExistedResourceException("Username đã tồn tại");
        }
        User newUser = User.builder()
                    .username(username)
                    .email(email)
                    .build();

        String password = userDTO.getPassword();
        String encodedPassword = passwordEncoder.encode(password);
        newUser.setPassword(encodedPassword);

        return userRepository.save(newUser);
    }

    @Override
    public User login(UserLoginDTO userDTO) throws Exception {
        User user = userRepository.findByUsername(userDTO.getUsername())
                .orElseThrow(() -> new DataNotFoundException("Người dùng không tồn tại"));
        if(!passwordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
            throw new PasswordNotMatchException("Mật khẩu không chính xác");
        }
        return user;
    }
}
