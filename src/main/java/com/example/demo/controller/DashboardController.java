package com.example.demo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/dashboard/admin")
    public String adminDashboard() {
        return "dashboard/admin";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/dashboard/user")
    public String userDashboard() {
        return "dashboard/user";
    }

    @PreAuthorize("hasRole('ROLE_DOCTOR')")
    @GetMapping("/dashboard/doctor")
    public String doctorDashboard() {
        return "dashboard/doctor";
    }
    
}
