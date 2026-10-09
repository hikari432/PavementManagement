
package com.example.pavementmanagement;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.pavementmanagement.model.Project;
import com.example.pavementmanagement.repository.ExpenseRepository;
import com.example.pavementmanagement.repository.ProjectRepository;
import com.example.pavementmanagement.repository.SalesRepository;

@Controller
public class ProjectController {

    private final ProjectRepository projectRepository;
    private final SalesRepository salesRepository;
    private final ExpenseRepository expenseRepository;

    public ProjectController(
            ProjectRepository projectRepository,
            SalesRepository salesRepository,
            ExpenseRepository expenseRepository) {

        this.projectRepository = projectRepository;
        this.salesRepository = salesRepository;
        this.expenseRepository = expenseRepository;
    }

    // 工事一覧
    @GetMapping("/projects")
    public String projects(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status,
            Model model) {

        List<Project> projects = projectRepository.findAll();

        // キーワード検索
        if (keyword != null && !keyword.isBlank()) {

            projects = projects.stream()
                    .filter(project ->
                        project.getProjectName().contains(keyword)
                        || project.getClientName().contains(keyword)
                    )
                    .toList();
        }

        // 状態で絞り込み
        if (status != null && !status.isBlank()) {

            projects = projects.stream()
                    .filter(project ->
                        project.getStatus().equals(status)
                    )
                    .toList();
        }

        model.addAttribute("projects", projects);
        model.addAttribute("keyword", keyword);
        model.addAttribute("status", status);

        return "projects";
    }

    // 工事登録画面
    @GetMapping("/projects/new")
    public String newProject(Model model) {

        model.addAttribute("project", new Project());

        return "projectForm";
    }

    // 工事登録処理
    @PostMapping("/projects")
    public String createProject(
            @Valid Project project,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "projectForm";
        }

        projectRepository.save(project);

        return "redirect:/projects";
    }

    // 工事編集画面
    @GetMapping("/projects/edit/{id}")
    public String editProject(
            @PathVariable int id,
            Model model) {

        Project project =
                projectRepository.findById(id).orElseThrow();

        model.addAttribute("project", project);

        return "projectEdit";
    }

    // 工事更新処理
    @PostMapping("/projects/edit/{id}")
    public String updateProject(
            @PathVariable int id,
            @Valid Project project,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {

            project.setId(id);
            model.addAttribute("project", project);

            return "projectEdit";
        }

        Project existingProject =
                projectRepository.findById(id).orElseThrow();

        existingProject.setProjectName(project.getProjectName());
        existingProject.setClientName(project.getClientName());
        existingProject.setStartDate(project.getStartDate());
        existingProject.setEndDate(project.getEndDate());
        existingProject.setStatus(project.getStatus());

        projectRepository.save(existingProject);

        return "redirect:/projects";
    }

    // 工事削除処理
    @PostMapping("/projects/delete/{id}")
    public String deleteProject(
            @PathVariable int id,
            RedirectAttributes redirectAttributes) {

        // 対象工事が存在するか確認
        if (!projectRepository.existsById(id)) {

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "指定された工事が見つかりません。");

            return "redirect:/projects";
        }

        // 売上が紐づいているか確認
        boolean hasSales = salesRepository.findAll().stream()
                .anyMatch(sales ->
                    sales.getProject() != null
                    && sales.getProject().getId() == id
                );

        // 経費が紐づいているか確認
        boolean hasExpenses = expenseRepository.findAll().stream()
                .anyMatch(expense ->
                    expense.getProject() != null
                    && expense.getProject().getId() == id
                );

        // 売上または経費がある場合は削除しない
        if (hasSales || hasExpenses) {

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "この工事には売上または経費が登録されているため、削除できません。");

            return "redirect:/projects";
        }

        // 紐づくデータがなければ削除
        projectRepository.deleteById(id);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "工事を削除しました。");

        return "redirect:/projects";
    }
}
