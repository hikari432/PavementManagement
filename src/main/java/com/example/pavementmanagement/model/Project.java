package com.example.pavementmanagement.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "工事名を入力してください")
    private String projectName;

    @NotBlank(message = "発注者を入力してください")
    private String clientName;

    @NotNull(message = "開始日を入力してください")
    private LocalDate startDate;

    @NotNull(message = "終了日を入力してください")
    private LocalDate endDate;

    @NotBlank(message = "状態を入力してください")
    private String status;

    public Project() {
    }

    public Project(int id, String projectName, String clientName,
                   LocalDate startDate, LocalDate endDate, String status) {
        this.id = id;
        this.projectName = projectName;
        this.clientName = clientName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    public Project(String projectName, String clientName,
                   LocalDate startDate, LocalDate endDate, String status) {
        this.projectName = projectName;
        this.clientName = clientName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}