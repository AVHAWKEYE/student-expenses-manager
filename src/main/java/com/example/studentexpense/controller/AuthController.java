package com.example.studentexpense.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    // ✅ Show the login page for both "/" and "/login"
    @GetMapping(value = {"/", "/login"})
    public String showLoginPage() {
        return "login";  // Looks for templates/login.html
    }

    // ✅ Handle login form submission
    @PostMapping("/login")
    public String processLogin(@RequestParam String username,
                               @RequestParam String password) {

        // TEMPORARY: simple demo validation
        if ("admin".equals(username) && "1234".equals(password)) {
            return "redirect:/dashboard";
        } else if ("hi".equals(username) && "1234".equals(password)) { // Example registered user
            return "redirect:/dashboard";
        } else {
            return "redirect:/login?error=true";
        }
    }
}
