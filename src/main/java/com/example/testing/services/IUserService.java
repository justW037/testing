package com.example.testing.services;

import com.example.testing.DTOs.UserDTO;
import com.example.testing.models.User;

public interface IUserService {
    public User createUser(UserDTO userDTO) throws Exception;
}
