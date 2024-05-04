package com.example.testing.controllers;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

import com.example.testing.services.user.IUserService;
import com.example.testing.services.user.UserService;
import com.example.testing.DTOs.UserLoginDTO;
import com.example.testing.DTOs.UserRegisterDTO;
import com.example.testing.models.User;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {
    public final IUserService userService;


    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("userLoginDTO", new UserLoginDTO());
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("userRegisterDTO", new UserRegisterDTO());
        return "register";
    }

    @PostMapping("/register")
    public String createUser(@Valid UserRegisterDTO userRegisterDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            model.addAttribute("errorMessages", errorMessages);
            return "register";
        }
        try {
            userService.createUser(userRegisterDTO);
            return "redirect:login"; 
        } catch (Exception e) {
            model.addAttribute("errorMessages", e.getMessage());
        }
        return "register";
    }

    @PostMapping("/login")
    public String login(@Valid UserLoginDTO userLoginDTO, BindingResult result, Model model, HttpSession session) {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            model.addAttribute("errorMessages", errorMessages);
            return "login";
        }
        try {
            User user = userService.login(userLoginDTO);
            session.setAttribute("user", user); 
            return "redirect:/students/home";
        } catch (Exception e) {
            model.addAttribute("errorMessages", e.getMessage());
        }
        return "login";
    }
}