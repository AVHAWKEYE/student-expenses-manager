package com.example.studentexpense.repository;

import com.example.studentexpense.model.Expense;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends MongoRepository<Expense, String> {
    List<Expense> findByUserId(String userId);
    List<Expense> findByUserIdAndDateBetween(String userId, LocalDate start, LocalDate end);
}
