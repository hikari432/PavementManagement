package com.example.pavementmanagement;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.pavementmanagement.model.Expense;
import com.example.pavementmanagement.model.Sales;
import com.example.pavementmanagement.repository.ExpenseRepository;
import com.example.pavementmanagement.repository.SalesRepository;

@Controller
public class ProfitController {

    private final SalesRepository salesRepository;
    private final ExpenseRepository expenseRepository;

    public ProfitController(
            SalesRepository salesRepository,
            ExpenseRepository expenseRepository) {
        this.salesRepository = salesRepository;
        this.expenseRepository = expenseRepository;
    }

    @GetMapping("/profit")
    public String profit(Model model) {

        int salesTotal = 0;
        int expenseTotal = 0;

        int materialCost = 0;
        int laborCost = 0;
        int machineCost = 0;
        int otherCost = 0;

        List<Sales> salesList = salesRepository.findAll();
        List<Expense> expenseList = expenseRepository.findAll();

        // 全体の売上を計算
        for (Sales sales : salesList) {
            salesTotal += sales.getAmount();
        }

        // 全体の経費を計算
        for (Expense expense : expenseList) {

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

        int profit = salesTotal - expenseTotal;

        // 月別集計
        List<MonthlyProfit> monthlyProfits = new ArrayList<>();

        for (Sales sales : salesList) {

            YearMonth yearMonth =
                    YearMonth.from(sales.getSalesDate());

            MonthlyProfit monthlyProfit =
                    findMonthlyProfit(monthlyProfits, yearMonth);

            if (monthlyProfit == null) {
                monthlyProfit =
                        new MonthlyProfit(yearMonth);

                monthlyProfits.add(monthlyProfit);
            }

            monthlyProfit.addSales(sales.getAmount());
        }

        for (Expense expense : expenseList) {

            YearMonth yearMonth =
                    YearMonth.from(expense.getPaymentDate());

            MonthlyProfit monthlyProfit =
                    findMonthlyProfit(monthlyProfits, yearMonth);

            if (monthlyProfit == null) {
                monthlyProfit =
                        new MonthlyProfit(yearMonth);

                monthlyProfits.add(monthlyProfit);
            }

            monthlyProfit.addExpense(expense.getAmount());
        }

        // 月順に並べる
        monthlyProfits.sort(
                (a, b) -> a.getYearMonth()
                           .compareTo(b.getYearMonth())
        );

        model.addAttribute("salesTotal", salesTotal);
        model.addAttribute("expenseTotal", expenseTotal);
        model.addAttribute("profit", profit);

        model.addAttribute("materialCost", materialCost);
        model.addAttribute("laborCost", laborCost);
        model.addAttribute("machineCost", machineCost);
        model.addAttribute("otherCost", otherCost);

        // 月別集計を画面に渡す
        model.addAttribute("monthlyProfits", monthlyProfits);

        return "profit";
    }

    // 指定した月のデータを探す
    private MonthlyProfit findMonthlyProfit(
            List<MonthlyProfit> monthlyProfits,
            YearMonth yearMonth) {

        for (MonthlyProfit monthlyProfit : monthlyProfits) {

            if (monthlyProfit.getYearMonth().equals(yearMonth)) {
                return monthlyProfit;
            }
        }

        return null;
    }

    // 月別の売上・経費・利益を入れるクラス
    public static class MonthlyProfit {

        private YearMonth yearMonth;
        private int salesTotal;
        private int expenseTotal;

        public MonthlyProfit(YearMonth yearMonth) {
            this.yearMonth = yearMonth;
        }

        public void addSales(int amount) {
            salesTotal += amount;
        }

        public void addExpense(int amount) {
            expenseTotal += amount;
        }

        public YearMonth getYearMonth() {
            return yearMonth;
        }

        public int getSalesTotal() {
            return salesTotal;
        }

        public int getExpenseTotal() {
            return expenseTotal;
        }

        public int getProfit() {
            return salesTotal - expenseTotal;
        }
    }
}