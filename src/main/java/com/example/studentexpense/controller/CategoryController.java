package com.example.studentexpense.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class CategoryController {

    private Map<String, Integer> categoryExpenses = new LinkedHashMap<>();

    @GetMapping("/categories")
    public String showCategories(Model model) {
        model.addAttribute("categoryExpenses", categoryExpenses);
        model.addAttribute("categories", categoryExpenses.keySet());
        return "categories";
    }

    @PostMapping("/categories/add")
    public String addCategory(@RequestParam String categoryName) {
        // If new, add with random spending value (simulate data)
        categoryExpenses.putIfAbsent(categoryName, new Random().nextInt(500) + 50);
        return "redirect:/categories";
    }

    @GetMapping("/categories/delete/{category}")
    public String deleteCategory(@PathVariable String category) {
        categoryExpenses.remove(category);
        return "redirect:/categories";
    }
}
