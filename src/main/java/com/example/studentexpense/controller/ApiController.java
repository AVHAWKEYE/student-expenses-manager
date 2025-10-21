package com.example.studentexpense.controller;

import com.example.studentexpense.model.Budget;
import com.example.studentexpense.model.Expense;
import com.example.studentexpense.repository.BudgetRepository;
import com.example.studentexpense.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
public class ApiController {
    @Autowired
    private ExpenseRepository expenseRepository;
    @Autowired
    private BudgetRepository budgetRepository;

    @PostMapping("/api/expenses")
    public Expense createExpense(@RequestBody Expense e) {
        if (e.getDate() == null) e.setDate(LocalDate.now());
        return expenseRepository.save(e);
    }

    @GetMapping("/api/expenses/user/{userId}")
    public List<Expense> getUserExpenses(@PathVariable String userId) {
        return expenseRepository.findByUserId(userId);
    }

    @PostMapping("/api/budgets")
    public Budget createBudget(@RequestBody Budget b) {
        return budgetRepository.save(b);
    }
}
