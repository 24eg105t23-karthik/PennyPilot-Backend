package com.pennypilot.backend;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
public class HelloController {

    private final ExpenseRepository expenseRepository;

    public HelloController(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    // Test the backend
    @GetMapping("/api/hello")
    public String hello() {
        return "PennyPilot backend is working!";
    }

    // Get all expenses
    @GetMapping("/api/expenses")
    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    // Get one expense by ID
    @GetMapping("/api/expenses/{id}")
    public ResponseEntity<Expense> getExpenseById(@PathVariable Long id) {
        return expenseRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Add a new expense
    @PostMapping("/api/expenses")
    public ResponseEntity<Expense> addExpense(@RequestBody Expense expense) {
        expense.setId(null);

        Expense savedExpense = expenseRepository.save(expense);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedExpense);
    }

    // Update an expense
    @PutMapping("/api/expenses/{id}")
    public ResponseEntity<Expense> updateExpense(
            @PathVariable Long id,
            @RequestBody Expense updatedExpense) {

        Optional<Expense> existing = expenseRepository.findById(id);

        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Expense expense = existing.get();
        expense.setTitle(updatedExpense.getTitle());
        expense.setAmount(updatedExpense.getAmount());
        expense.setCategory(updatedExpense.getCategory());
        expense.setExpenseDate(updatedExpense.getExpenseDate());
        expense.setDescription(updatedExpense.getDescription());

        return ResponseEntity.ok(expenseRepository.save(expense));
    }

    // Delete an expense
    @DeleteMapping("/api/expenses/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        if (!expenseRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        expenseRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}