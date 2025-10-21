package com.example.studentexpense.controller;

import com.example.studentexpense.model.Expense;
import com.example.studentexpense.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
public class ReportController {

    @Autowired
    private ExpenseRepository expenseRepository;

    @GetMapping("/reports")
    public String showReports(Model model) {
        List<Expense> expenses = expenseRepository.findAll();

        Map<String, Double> yearly = new HashMap<>();
        Map<String, Double> monthly = new HashMap<>();
        Map<String, Double> weekly = new HashMap<>();

        LocalDate today = LocalDate.now();

        // Initialize maps
        for (int i = 1; i <= 12; i++) yearly.put(String.valueOf(i), 0.0);
        for (int i = 1; i <= 4; i++) monthly.put("Week " + i, 0.0);
        for (String day : Arrays.asList("Mon","Tue","Wed","Thu","Fri","Sat","Sun")) weekly.put(day, 0.0);

        for (Expense e : expenses) {
            LocalDate date = LocalDate.parse(e.getDate(), DateTimeFormatter.ISO_DATE);

            // Yearly
            if (date.getYear() == today.getYear()) {
                String month = String.valueOf(date.getMonthValue());
                yearly.put(month, yearly.get(month) + e.getAmount());
            }

            // Monthly
            if (date.getMonth() == today.getMonth() && date.getYear() == today.getYear()) {
                int week = (date.getDayOfMonth() - 1) / 7 + 1;
                monthly.put("Week " + week, monthly.get("Week " + week) + e.getAmount());
            }

            // Weekly
            if (date.isAfter(today.minusDays(7))) {
                String day = date.getDayOfWeek().name().substring(0,3);
                weekly.put(day, weekly.get(day) + e.getAmount());
            }
        }

        model.addAttribute("yearlyData", yearly.values());
        model.addAttribute("monthlyData", monthly.values());
        model.addAttribute("weeklyData", weekly.values());

        return "reports";
    }
}
