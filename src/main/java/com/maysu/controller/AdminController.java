package com.maysu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.access.prepost.PreAuthorize;

@Controller
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @GetMapping("/admin")
    public String dashboard() {
        return "admin/index"; // Vista principal del panel
    }
}
