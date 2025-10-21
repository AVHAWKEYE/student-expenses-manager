package com.example.studentexpense.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        model.addAttribute("pageTitle", "Dashboard");
        return "dashboard";  // templates/dashboard.html
    }

    @GetMapping("/expenses")
    public String showExpenses() {
        return "expenses";  // templates/expenses.html
    }

    @GetMapping("/dashboard/budgets")
    public String showBudgetsDashboard() {
        return "dashboard-budgets";
    }


//    @GetMapping("/dashboard/categories")
//    public String showCategories() {
//        // ...
//    }


    @GetMapping("/chatbot")
    public String showChatbot() {
        return "chatbot"; // templates/chatbot.html
    }
}
