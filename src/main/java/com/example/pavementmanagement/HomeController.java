
package com.example.pavementmanagement;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.pavementmanagement.model.Expense;
import com.example.pavementmanagement.model.Sales;
import com.example.pavementmanagement.repository.ExpenseRepository;
import com.example.pavementmanagement.repository.ProjectRepository;
import com.example.pavementmanagement.repository.SalesRepository;

@Controller
public class HomeController {

    private final ProjectRepository projectRepository;
    private final SalesRepository salesRepository;
    private final ExpenseRepository expenseRepository;

    public HomeController(
            ProjectRepository projectRepository,
            SalesRepository salesRepository,
            ExpenseRepository expenseRepository) {

        this.projectRepository = projectRepository;
        this.salesRepository = salesRepository;
        this.expenseRepository = expenseRepository;
    }

    @GetMapping("/")
    public String index(Model model, Authentication authentication) {

        // 工事件数
        int projectCount = projectRepository.findAll().size();

        // 売上・経費・未入金額
        int salesTotal = 0;
        int expenseTotal = 0;
        int unpaidTotal = 0;

        // 売上の集計
        for (Sales sales : salesRepository.findAll()) {

            salesTotal += sales.getAmount();

            if ("未入金".equals(sales.getPaymentStatus())) {
                unpaidTotal += sales.getAmount();
            }
        }

        // 経費の集計
        for (Expense expense : expenseRepository.findAll()) {
            expenseTotal += expense.getAmount();
        }

        // 利益の計算
        int profit = salesTotal - expenseTotal;

        // 集計結果をHTMLへ渡す
        model.addAttribute("projectCount", projectCount);
        model.addAttribute("salesTotal", salesTotal);
        model.addAttribute("expenseTotal", expenseTotal);
        model.addAttribute("profit", profit);
        model.addAttribute("unpaidTotal", unpaidTotal);

        // ログインユーザー情報をHTMLへ渡す
        String username = "ゲスト";
        boolean isAdmin = false;

        if (authentication != null
                && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getName())) {

            username = authentication.getName();

            isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(authority ->
                            "ROLE_ADMIN".equals(authority.getAuthority()));
        }

        model.addAttribute("username", username);
        model.addAttribute("isAdmin", isAdmin);

        return "index";
    }

    @GetMapping("/accessDenied")
    public String accessDenied() {
        return "accessDenied";
    }
}

 