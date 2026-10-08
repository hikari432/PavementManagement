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
public class Sales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "売上名を入力してください")
    private String salesName;

    @ManyToOne
    private Project project;

    @Min(value = 1, message = "金額は1円以上で入力してください")
    private int amount;

    @NotNull(message = "売上日を入力してください")
    private LocalDate salesDate;

    @NotBlank(message = "入金状況を選択してください")
    private String paymentStatus;

    public Sales() {
    }

    public Sales(int id, String salesName, Project project,
                 int amount, LocalDate salesDate, String paymentStatus) {
        this.id = id;
        this.salesName = salesName;
        this.project = project;
        this.amount = amount;
        this.salesDate = salesDate;
        this.paymentStatus = paymentStatus;
    }

    public Sales(String salesName, Project project,
                 int amount, LocalDate salesDate, String paymentStatus) {
        this.salesName = salesName;
        this.project = project;
        this.amount = amount;
        this.salesDate = salesDate;
        this.paymentStatus = paymentStatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSalesName() {
        return salesName;
    }

    public void setSalesName(String salesName) {
        this.salesName = salesName;
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

    public LocalDate getSalesDate() {
        return salesDate;
    }

    public void setSalesDate(LocalDate salesDate) {
        this.salesDate = salesDate;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}