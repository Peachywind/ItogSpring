package com.example.demo.controller;

import com.example.demo.model.Role;
import com.example.demo.service.RoleService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/roles")
public class RoleController {

    private final RoleService roleService;

    // Конструктор без @Autowired, так как это избыточно
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping()
    public String index(Model model) {
        model.addAttribute("roles", roleService.findAll());
        return "roles/index";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/new")
    public String newRole(Model model) {
        model.addAttribute("role", new Role());
        return "roles/new";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping()
    public String create(@ModelAttribute("role") @Valid Role role, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "roles/new";
        }
        roleService.save(role);
        return "redirect:/roles";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}/edit")
    public String editRole(@PathVariable("id") Long id, Model model) {
        Optional<Role> role = roleService.findById(id);
        if (role.isPresent()) {
            model.addAttribute("role", role.get());
            return "roles/edit";
        } else {
            return "redirect:/roles";
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/{id}")
    public String updateRole(@PathVariable("id") Long id, @ModelAttribute("role") @Valid Role role, BindingResult result) {
        if (result.hasErrors()) {
            return "roles/edit";
        }
        roleService.update(id, role);
        return "redirect:/roles";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/{id}/delete")
    public String deleteRole(@PathVariable("id") Long id) {
        roleService.deleteById(id);
        return "redirect:/roles";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/search")
    public String search(@RequestParam("name") String name, Model model) {
        List<Role> roles = roleService.findByNameLike(name);
        model.addAttribute("roles", roles);
        return "roles/index";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public String showRole(@PathVariable("id") Long id, Model model) {
        Optional<Role> role = roleService.findById(id);
        if (role.isPresent()) {
            model.addAttribute("role", role.get());
            return "roles/show";
        } else {
            return "redirect:/roles";
        }
    }
}
