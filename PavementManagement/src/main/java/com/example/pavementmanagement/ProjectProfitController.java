package com.example.pavementmanagement;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.pavementmanagement.model.Expense;
import com.example.pavementmanagement.model.Project;
import com.example.pavementmanagement.model.Sales;
import com.example.pavementmanagement.repository.ExpenseRepository;
import com.example.pavementmanagement.repository.ProjectRepository;
import com.example.pavementmanagement.repository.SalesRepository;

@Controller
public class ProjectProfitController {

    private final ProjectRepository projectRepository;
    private final SalesRepository salesRepository;
    private final ExpenseRepository expenseRepository;

    public ProjectProfitController(
            ProjectRepository projectRepository,
            SalesRepository salesRepository,
            ExpenseRepository expenseRepository) {
        this.projectRepository = projectRepository;
        this.salesRepository = salesRepository;
        this.expenseRepository = expenseRepository;
    }

    @GetMapping("/project-profit")
    public String projectProfit(Model model) {

        List<ProjectProfit> projectProfits = new ArrayList<>();

        List<Project> projects = projectRepository.findAll();
        List<Sales> salesList = salesRepository.findAll();
        List<Expense> expenseList = expenseRepository.findAll();

        for (Project project : projects) {

            int salesTotal = 0;
            int expenseTotal = 0;

            for (Sales sales : salesList) {
                if (sales.getProject() != null
                        && project.getId() == sales.getProject().getId()) {

                    salesTotal += sales.getAmount();
                }
            }

            for (Expense expense : expenseList) {
                if (expense.getProject() != null
                        && project.getId() == expense.getProject().getId()) {

                    expenseTotal += expense.getAmount();
                }
            }

            int profit = salesTotal - expenseTotal;

            double profitRate = 0;

            if (salesTotal > 0) {
                profitRate = (double) profit / salesTotal * 100;
            }

            projectProfits.add(
                new ProjectProfit(
                    project.getProjectName(),
                    salesTotal,
                    expenseTotal,
                    profit,
                    profitRate
                )
            );
        }

        model.addAttribute("projectProfits", projectProfits);

        return "projectProfit";
    }

    @GetMapping("/project-profit/{projectName}")
    public String projectProfitDetail(
            @PathVariable String projectName,
            Model model) {

        int salesTotal = 0;
        int expenseTotal = 0;

        int materialCost = 0;
        int laborCost = 0;
        int machineCost = 0;
        int otherCost = 0;

        List<Sales> salesList = salesRepository.findAll();
        List<Expense> expenseList = expenseRepository.findAll();

        for (Sales sales : salesList) {

            if (sales.getProject() != null
                    && projectName.equals(
                        sales.getProject().getProjectName())) {

                salesTotal += sales.getAmount();
            }
        }

        for (Expense expense : expenseList) {

            if (expense.getProject() != null
                    && projectName.equals(
                        expense.getProject().getProjectName())) {

                expenseTotal += expense.getAmount();

                if ("材料費".equals(expense.getCategory())) {
                    materialCost += expense.getAmount();

                } else if ("人件費".equals(expense.getCategory())) {
                    laborCost += expense.getAmount();

                } else if ("機械費".equals(expense.getCategory())) {
                    machineCost += expense.getAmount();

                } else if ("その他".equals(expense.getCategory())) {
                    otherCost += expense.getAmount();
                }
            }
        }

        int profit = salesTotal - expenseTotal;

        double profitRate = 0;

        if (salesTotal > 0) {
            profitRate = (double) profit / salesTotal * 100;
        }

        model.addAttribute("projectName", projectName);
        model.addAttribute("salesTotal", salesTotal);
        model.addAttribute("expenseTotal", expenseTotal);
        model.addAttribute("profit", profit);
        model.addAttribute("profitRate", profitRate);
        model.addAttribute("materialCost", materialCost);
        model.addAttribute("laborCost", laborCost);
        model.addAttribute("machineCost", machineCost);
        model.addAttribute("otherCost", otherCost);

        return "projectProfitDetail";
    }

    public static class ProjectProfit {

        private String projectName;
        private int salesTotal;
        private int expenseTotal;
        private int profit;
        private double profitRate;

        public ProjectProfit(
                String projectName,
                int salesTotal,
                int expenseTotal,
                int profit,
                double profitRate) {

            this.projectName = projectName;
            this.salesTotal = salesTotal;
            this.expenseTotal = expenseTotal;
            this.profit = profit;
            this.profitRate = profitRate;
        }

        public String getProjectName() {
            return projectName;
        }

        public int getSalesTotal() {
            return salesTotal;
        }

        public int getExpenseTotal() {
            return expenseTotal;
        }

        public int getProfit() {
            return profit;
        }

        public double getProfitRate() {
            return profitRate;
        }
    }
}