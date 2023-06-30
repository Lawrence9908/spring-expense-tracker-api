package com.lawrence.expensetraker.controller;

import com.lawrence.expensetraker.entity.Expense;
import com.lawrence.expensetraker.services.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
public class ExpenseController {
   private ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }
    //Get all the expenses
    @GetMapping("/expenses")
    public List<Expense> getAllExpenses(Pageable  page){
        return expenseService.getAllExpenses(page).toList();
    }
    //Get expense by id
    @GetMapping("/expenses/{id}")
    public Expense getExpenseById(@PathVariable Long id){
        return getExpenseById(id);
    }

    //Get expenses by category
    @GetMapping("/expenses/category")
    public List<Expense> getExpensesByCategory(@RequestParam String category, Pageable page){
        return expenseService.readByCategory(category, page);
    }
    //Get expense by date
    @GetMapping("/expenses/date")
    public List<Expense> getExpensesByDates(@RequestParam(required = true) Date startDate,
                                            @RequestParam(required = true) Date endDate,
                                            Pageable page){
        return expenseService.readByDate(startDate, endDate, page);
    }

    //Get expense by name
    public List<Expense> getExpensesByName(@RequestParam String keyword, Pageable page){
        return expenseService.readByName(keyword, page);
    }
    //delete expense by id
    @DeleteMapping("/expenses/{id}")
    public void deleteExpenseById(@PathVariable Long id){
        expenseService.deleteExpenseById(id);
    }

    // Add new Expense
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/expense")
    public Expense saveExpenseDetails(@Valid @RequestBody Expense expense){
        return expenseService.saveExpenseDetails(expense);
    }
    //Update expense
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("expenses/{id}")
    public Expense updateExpenseDetails(@Valid @RequestBody Expense expense,
                                        @PathVariable Long id){
        return expenseService.updateExpenseDetails(id, expense);
    }
}
