package com.example.studentexpense.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class RegisterController {

    // ✅ Show the registration page
    @GetMapping("/register")
    public String showRegisterForm() {
        return "register"; // Looks for templates/register.html
    }

    // ✅ Handle registration form submission
    @PostMapping("/register")
    public String processRegister(@RequestParam String username,
                                  @RequestParam String password,
                                  Model model) {
        // TODO: Save user to MongoDB (for now, just print to console)
        System.out.println("Registered user: " + username + " | Password: " + password);

        // Redirect to login after registration
        model.addAttribute("message", "Registration successful! Please log in.");
        return "redirect:/login";
    }
}
