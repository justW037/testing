package com.example.testing.services.user;

import com.example.testing.DTOs.UserLoginDTO;
import com.example.testing.DTOs.UserRegisterDTO;
import com.example.testing.models.User;

public interface IUserService {
    public User createUser(UserRegisterDTO userDTO) throws Exception;
    public User login(UserLoginDTO userDTO) throws Exception;
}
