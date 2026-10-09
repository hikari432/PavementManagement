package com.example.pavementmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pavementmanagement.model.Expense;

public interface ExpenseRepository extends JpaRepository<Expense, Integer> {

}
