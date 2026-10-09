package com.example.pavementmanagement;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.pavementmanagement.model.Expense;
import com.example.pavementmanagement.repository.ExpenseRepository;
import com.example.pavementmanagement.repository.ProjectRepository;

@Controller
public class ExpenseController {

    private final ExpenseRepository expenseRepository;
    private final ProjectRepository projectRepository;

    public ExpenseController(
            ExpenseRepository expenseRepository,
            ProjectRepository projectRepository) {
        this.expenseRepository = expenseRepository;
        this.projectRepository = projectRepository;
    }

    @GetMapping("/expenses")
    public String expenses(Model model) {
        model.addAttribute("expenses", expenseRepository.findAll());
        return "expenses";
    }

    @GetMapping("/expenses/new")
    public String newExpense(Model model) {
        model.addAttribute("expense", new Expense());
        model.addAttribute("projects", projectRepository.findAll());
        return "expenseForm";
    }

    @PostMapping("/expenses")
    public String createExpense(
            @Valid Expense expense,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("projects", projectRepository.findAll());
            return "expenseForm";
        }

        expenseRepository.save(expense);

        return "redirect:/expenses";
    }

    @GetMapping("/expenses/edit/{id}")
    public String editExpense(@PathVariable int id, Model model) {

        Expense expense = expenseRepository.findById(id).orElseThrow();

        model.addAttribute("expense", expense);
        model.addAttribute("projects", projectRepository.findAll());

        return "expenseEdit";
    }

    @PostMapping("/expenses/edit/{id}")
    public String updateExpense(
            @PathVariable int id,
            @Valid Expense expense,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            expense.setId(id);
            model.addAttribute("expense", expense);
            model.addAttribute("projects", projectRepository.findAll());
            return "expenseEdit";
        }

        Expense existingExpense =
                expenseRepository.findById(id).orElseThrow();

        existingExpense.setExpenseName(expense.getExpenseName());
        existingExpense.setProject(expense.getProject());
        existingExpense.setAmount(expense.getAmount());
        existingExpense.setPaymentDate(expense.getPaymentDate());
        existingExpense.setCategory(expense.getCategory());

        expenseRepository.save(existingExpense);

        return "redirect:/expenses";
    }

    @PostMapping("/expenses/delete/{id}")
    public String deleteExpense(@PathVariable int id) {
        expenseRepository.deleteById(id);
        return "redirect:/expenses";
    }
}