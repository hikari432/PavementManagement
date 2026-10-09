package com.example.pavementmanagement.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "経費名を入力してください")
    private String expenseName;

    @ManyToOne
    private Project project;

    @Min(value = 1, message = "金額は1円以上で入力してください")
    private int amount;

    @NotNull(message = "支払日を入力してください")
    private LocalDate paymentDate;

    @NotBlank(message = "経費区分を選択してください")
    private String category;

    public Expense() {
    }

    public Expense(int id, String expenseName, Project project,
                   int amount, LocalDate paymentDate, String category) {
        this.id = id;
        this.expenseName = expenseName;
        this.project = project;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.category = category;
    }

    public Expense(String expenseName, Project project,
                   int amount, LocalDate paymentDate, String category) {
        this.expenseName = expenseName;
        this.project = project;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getExpenseName() {
        return expenseName;
    }

    public void setExpenseName(String expenseName) {
        this.expenseName = expenseName;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}