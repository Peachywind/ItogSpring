package com.example.demo.controller;

import com.example.demo.model.Department;
import com.example.demo.service.DepartmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    @Autowired
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping()
    public String index(Model model) {
        model.addAttribute("departments", departmentService.findAll());
        return "departments/index";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/new")
    public String newDepartment(Model model) {
        model.addAttribute("department", new Department());
        return "departments/new";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping()
    public String create(@ModelAttribute("department") @Valid Department department, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "departments/new";
        }
        departmentService.save(department);
        return "redirect:/departments";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}/edit")
    public String editDepartment(@PathVariable("id") Long id, Model model) {
        Optional<Department> department = departmentService.findById(id);
        if (department.isPresent()) {
            model.addAttribute("department", department.get());
            return "departments/edit";
        } else {
            return "redirect:/departments";
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/{id}")
    public String updateDepartment(@PathVariable("id") Long id, @ModelAttribute("department") @Valid Department department, BindingResult result) {
        if (result.hasErrors()) {
            return "departments/edit";
        }
        departmentService.update(id, department);
        return "redirect:/departments";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/{id}/delete")
    public String deleteDepartment(@PathVariable("id") Long id) {
        departmentService.deleteById(id);
        return "redirect:/departments";
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public String showDepartment(@PathVariable("id") Long id, Model model) {
        Optional<Department> department = departmentService.findById(id);
        if (department.isPresent()) {
            model.addAttribute("department", department.get());
            return "departments/show";
        } else {
            return "redirect:/departments";
        }
    }
    
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/search")
    public String search(@RequestParam("name") String name, Model model) {
        List<Department> departments = departmentService.findByNameContaining(name);
        model.addAttribute("departments", departments);
        return "departments/index";
    }
    
}



