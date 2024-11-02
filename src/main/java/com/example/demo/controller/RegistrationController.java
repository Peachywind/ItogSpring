package com.example.demo.controller;

import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.service.RoleService;
import com.example.demo.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Collections;

@Controller
@RequestMapping("/register")
public class RegistrationController {

    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public RegistrationController(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService; // Инициализация RoleService
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }

    @PostMapping
    public String registerUser(@ModelAttribute("user") @Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "auth/register";
        }

        if (userService.findByUsername(user.getUsername()).isPresent()) {
            model.addAttribute("usernameError", "Пользователь с таким именем уже существует");
            return "auth/register";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // Назначение роли USER
        Role userRole = roleService.findByName("USER");
        if (userRole != null) {
            user.setRoles(Collections.singletonList(userRole)); // Установите роль пользователю
        }

        userService.save(user);
        return "redirect:/login";
    }
}
