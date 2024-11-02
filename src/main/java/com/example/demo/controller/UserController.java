package com.example.demo.controller;

import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.service.RoleService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping()
    public String index(Model model) {
        model.addAttribute("users", userService.findAll());
        return "users/index"; // Путь к шаблону
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/new")
    public String newUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", roleService.findAll()); // Добавляем все роли в модель
        return "users/new"; // Путь к шаблону
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping()
    public String create(@ModelAttribute("user") @Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("allRoles", roleService.findAll()); // Добавляем роли в модель при ошибках
            return "users/new";
        }

        // Хэширование пароля перед сохранением
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userService.save(user);
        return "redirect:/users"; // Перенаправление после создания
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}/edit")
    public String editUser(@PathVariable("id") Long id, Model model) {
        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            model.addAttribute("allRoles", roleService.findAll()); // Добавляем все роли в модель
            return "users/edit"; // Путь к шаблону редактирования
        } else {
            return "redirect:/users"; // Перенаправление, если пользователь не найден
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/{id}")
    public String updateUser(@PathVariable("id") Long id, @ModelAttribute("user") @Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("allRoles", roleService.findAll()); // Добавляем роли в модель при ошибках
            return "users/edit"; // Путь к шаблону редактирования
        }

        // Хэширование пароля при обновлении, если он изменен
        if (!user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        userService.update(id, user);
        return "redirect:/users"; // Перенаправление после обновления
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/{id}/delete")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteById(id);
        return "redirect:/users"; // Перенаправление после удаления
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/search")
    public String search(@RequestParam("username") String username, Model model) {
        List<User> users = userService.findByUsernameContaining(username);
        model.addAttribute("users", users);
        return "users/index"; // Путь к шаблону поиска
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public String showUser(@PathVariable("id") Long id, Model model) {
        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            return "users/show"; // Путь к шаблону просмотра
        } else {
            return "redirect:/users"; // Перенаправление, если пользователь не найден
        }
    }
}
