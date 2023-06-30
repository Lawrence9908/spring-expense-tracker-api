package com.lawrence.expensetraker.services.impl;

import com.lawrence.expensetraker.entity.Expense;
import com.lawrence.expensetraker.exceptions.ResourceNotFoundException;
import com.lawrence.expensetraker.repository.ExpenseRepository;
import com.lawrence.expensetraker.repository.UserRepository;
import com.lawrence.expensetraker.services.ExpenseService;
import com.lawrence.expensetraker.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
@Service
public class ExpenseServiceImpl implements ExpenseService {
    private ExpenseRepository expenseRepository;
    private UserService userService;

    public ExpenseServiceImpl(ExpenseRepository expenseRepository, UserService userService) {
        this.expenseRepository = expenseRepository;
        this.userService = userService;
    }

    @Override
    public Page<Expense> getAllExpenses(Pageable page) {
        return expenseRepository.findByUserId(userService.getLoggedInUser().getId(), page);
    }



    @Override
    public Expense getExpenseById(Long id) {
        Optional<Expense> expense  = expenseRepository.findByUserIdAndId(userService.getLoggedInUser().getId(), id);
        if(expense.isPresent()){
            return expense.get();
        }
        throw new ResourceNotFoundException("Expense with the id: "+ id +" not found!");
    }

    @Override
    public void deleteExpenseById(Long id) {
        Expense expense  = getExpenseById(id);
        expenseRepository.deleteById(id);
    }

    @Override
    public Expense saveExpenseDetails(Expense expense) {
        expense.setUser(userService.getLoggedInUser());
        return expenseRepository.save(expense);
    }

    @Override
    public Expense updateExpenseDetails(Long id, Expense expense) {
        Expense expenseToUpdate = getExpenseById(id);

        expenseToUpdate.setName(expense.getName());
        expenseToUpdate.setAmount(expense.getAmount());
        expenseToUpdate.setCategory(expense.getCategory());
        expenseToUpdate.setDescription(expense.getDescription());
        expenseToUpdate.setDate(expense.getDate());

        return expenseRepository.save(expenseToUpdate);
    }

    @Override
    public List<Expense> readByCategory(String category, Pageable page) {
        return expenseRepository.findByUserIdAndCategory(userService.getLoggedInUser().getId(),
                category, page).toList();
    }

    @Override
    public List<Expense> readByName(String keyword, Pageable page) {
        return expenseRepository.findByUserIdAndNameContaining(userService.getLoggedInUser().getId(),
                keyword, page).toList();
    }

    @Override
    public List<Expense> readByDate(Date startDate, Date endDate, Pageable page) {
        if(startDate == null){
            startDate = new Date(0);
        }
        if(endDate == null){
            endDate = new Date(System.currentTimeMillis());
        }
        return expenseRepository.findByUserIdAndDateBetween(userService.getLoggedInUser().getId(),
                startDate, endDate, page).toList();
    }
}
