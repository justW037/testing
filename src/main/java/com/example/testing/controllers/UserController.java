package com.example.testing.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.testing.services.IUserService;
import com.example.testing.services.UserService;
import com.example.testing.DTOs.UserDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {
    public final IUserService userService;

    @GetMapping("/login")
    public String showLoginForm() {
        // model.addAttribute("userDTO", new UserDTO());
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "register";
    }

    @PostMapping("/register")
    public String createUser(@Valid UserDTO userDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            model.addAttribute("errorMessages", errorMessages);
            return "register";
        }
        try {
            userService.createUser(userDTO);
            return "login";
        } catch (Exception e) {
            model.addAttribute("errorMessages", e);
        }
        return "register";
    }
}