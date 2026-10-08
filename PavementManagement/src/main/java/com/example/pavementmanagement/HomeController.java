 
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

        int projectCount = projectRepository.findAll().size();

        int salesTotal = 0;
        int expenseTotal = 0;
        int unpaidTotal = 0;

        for (Sales sales : salesRepository.findAll()) {

            salesTotal += sales.getAmount();

            if ("未入金".equals(sales.getPaymentStatus())) {
                unpaidTotal += sales.getAmount();
            }
        }

        for (Expense expense : expenseRepository.findAll()) {
            expenseTotal += expense.getAmount();
        }

        int profit = salesTotal - expenseTotal;

        model.addAttribute("projectCount", projectCount);
        model.addAttribute("salesTotal", salesTotal);
        model.addAttribute("expenseTotal", expenseTotal);
        model.addAttribute("profit", profit);
        model.addAttribute("unpaidTotal", unpaidTotal);

        // ログインユーザー情報をHTMLに渡す
        if (authentication != null) {

            model.addAttribute("username", authentication.getName());

            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(authority ->
                            "ROLE_ADMIN".equals(authority.getAuthority()));

            model.addAttribute("isAdmin", isAdmin);
        }

        return "index";
    }

    @GetMapping("/accessDenied")
    public String accessDenied() {
        return "accessDenied";
    }
}
 