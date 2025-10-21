package com.example.studentexpense.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class BudgetController {

    private List<Map<String, String>> budgets = new ArrayList<>();

    // ✅ Show the budgets page
    @GetMapping("/budgets")
    public String showBudgetsPage(Model model) {
        model.addAttribute("budgets", budgets);
        return "budgets"; // Loads templates/budgets.html
    }

    // ✅ Handle adding new budget
    @PostMapping("/budgets/add")
    public String addBudget(@RequestParam String category,
                            @RequestParam String limit) {

        Map<String, String> budget = new HashMap<>();
        budget.put("category", category);
        budget.put("limit", limit);
        budget.put("date", new Date().toString());

        budgets.add(budget);
        return "redirect:/budgets";
    }

    // ✅ Delete budget by index
    @GetMapping("/budgets/delete/{index}")
    public String deleteBudget(@PathVariable int index) {
        if (index >= 0 && index < budgets.size()) {
            budgets.remove(index);
        }
        return "redirect:/budgets";
    }
}
