package com.example.studentexpense.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class ExpenseController {

    // In-memory data store for demo (replace later with DB)
    private final List<Map<String, Object>> expenses = new ArrayList<>();

    // ✅ Display all expenses and totals
    @GetMapping("/manage-expenses")
    public String showExpenses(Model model) {
        model.addAttribute("expenses", expenses);

        // ✅ Group expenses by category
        Map<String, Double> categoryTotals = expenses.stream()
                .collect(Collectors.groupingBy(
                        e -> e.get("category").toString(),
                        Collectors.summingDouble(e -> Double.parseDouble(e.get("amount").toString()))
                ));

        // ✅ Compute grand total sum
        double totalSum = categoryTotals.values().stream()
                .mapToDouble(Double::doubleValue)
                .sum();

        // Add to model for Thymeleaf
        model.addAttribute("categoryTotals", categoryTotals);
        model.addAttribute("totalSum", totalSum);

        return "expenses"; // Renders templates/expenses.html
    }

    // ✅ Handle new expense form submission
    @PostMapping("/expenses/add")
    public String addExpense(@RequestParam String name,
                             @RequestParam String amount,
                             @RequestParam String category) {

        Map<String, Object> expense = new HashMap<>();
        expense.put("id", UUID.randomUUID().toString());
        expense.put("name", name);
        expense.put("amount", amount);
        expense.put("category", category);
        expense.put("date", new Date());

        expenses.add(expense);
        return "redirect:/manage-expenses";
    }

    // ✅ Delete an expense by ID
    @GetMapping("/expenses/delete/{id}")
    public String deleteExpense(@PathVariable String id) {
        expenses.removeIf(e -> e.get("id").equals(id));
        return "redirect:/manage-expenses";
    }

    // ✅ Optional: clear all expenses
    @GetMapping("/expenses/clear")
    public String clearExpenses() {
        expenses.clear();
        return "redirect:/manage-expenses";
    }
}
