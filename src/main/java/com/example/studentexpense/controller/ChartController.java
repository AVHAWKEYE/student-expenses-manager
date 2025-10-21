package com.example.studentexpense.controller;

import com.example.studentexpense.model.Expense;
import com.example.studentexpense.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.Date;
import java.util.stream.Collectors;

@RestController
public class ChartController {
    @Autowired
    private ExpenseRepository expenseRepository;

    // Example: /api/expenses/year?userId=...
    @GetMapping("/api/expenses/year")
    public Map<String, Double> year(@RequestParam String userId, @RequestParam int year) {
        LocalDate start = LocalDate.of(year,1,1);
        LocalDate end = LocalDate.of(year,12,31);
        List<Expense> list = expenseRepository.findByUserIdAndDateBetween(userId, start, end);
        Map<Integer, Double> monthly = new TreeMap<>();
        for (Expense e: list) {
            int m = e.getDate().getMonthValue();
            monthly.put(m, monthly.getOrDefault(m,0.0)+e.getAmount());
        }
        // convert to month name -> value
        Map<String, Double> out = new LinkedHashMap<>();
        for (int i=1;i<=12;i++) {
            out.put(String.valueOf(i), monthly.getOrDefault(i,0.0));
        }
        return out;
    }

    @GetMapping("/api/expenses/month")
    public Map<String, Double> month(@RequestParam String userId, @RequestParam int year, @RequestParam int month) {
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());
        List<Expense> list = expenseRepository.findByUserIdAndDateBetween(userId, start, end);
        Map<Integer, Double> daily = new TreeMap<>();
        for (Expense e: list) {
            int d = e.getDate().getDayOfMonth();
            daily.put(d, daily.getOrDefault(d,0.0)+e.getAmount());
        }
        Map<String, Double> out = new LinkedHashMap<>();
        for (int i=1;i<=start.lengthOfMonth();i++) {
            out.put(String.valueOf(i), daily.getOrDefault(i,0.0));
        }
        return out;
    }

    @GetMapping("/api/expenses/week")
    public Map<String, Double> week(@RequestParam String userId, @RequestParam int year, @RequestParam int week) {
        WeekFields wf = WeekFields.ISO;
        LocalDate first = LocalDate.ofYearDay(year,1);
        LocalDate start = first.with(wf.weekOfWeekBasedYear(), week).with(wf.dayOfWeek(), 1);
        LocalDate end = start.plusDays(6);
        List<Expense> list = expenseRepository.findByUserIdAndDateBetween(userId, start, end);
        Map<Integer, Double> daymap = new TreeMap<>();
        for (Expense e: list) {
            int d = e.getDate().getDayOfWeek().getValue();
            daymap.put(d, daymap.getOrDefault(d,0.0)+e.getAmount());
        }
        Map<String, Double> out = new LinkedHashMap<>();
        for (int i=1;i<=7;i++) out.put(String.valueOf(i), daymap.getOrDefault(i,0.0));
        return out;
    }
}
