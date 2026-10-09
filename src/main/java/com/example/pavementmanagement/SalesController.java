package com.example.pavementmanagement;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.pavementmanagement.model.Sales;
import com.example.pavementmanagement.repository.ProjectRepository;
import com.example.pavementmanagement.repository.SalesRepository;

@Controller
public class SalesController {

    private final SalesRepository salesRepository;
    private final ProjectRepository projectRepository;

    public SalesController(
            SalesRepository salesRepository,
            ProjectRepository projectRepository) {
        this.salesRepository = salesRepository;
        this.projectRepository = projectRepository;
    }

    @GetMapping("/sales")
    public String sales(Model model) {
        model.addAttribute("sales", salesRepository.findAll());
        return "sales";
    }

    @GetMapping("/sales/new")
    public String newSales(Model model) {
        model.addAttribute("sales", new Sales());
        model.addAttribute("projects", projectRepository.findAll());
        return "salesForm";
    }

    @PostMapping("/sales")
    public String createSales(
            @Valid Sales sales,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("projects", projectRepository.findAll());
            return "salesForm";
        }

        salesRepository.save(sales);

        return "redirect:/sales";
    }

    @GetMapping("/sales/edit/{id}")
    public String editSales(@PathVariable int id, Model model) {

        Sales sales = salesRepository.findById(id).orElseThrow();

        model.addAttribute("sales", sales);
        model.addAttribute("projects", projectRepository.findAll());

        return "salesEdit";
    }

    @PostMapping("/sales/edit/{id}")
    public String updateSales(
            @PathVariable int id,
            @Valid Sales sales,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            sales.setId(id);
            model.addAttribute("sales", sales);
            model.addAttribute("projects", projectRepository.findAll());
            return "salesEdit";
        }

        Sales existingSales =
                salesRepository.findById(id).orElseThrow();

        existingSales.setSalesName(sales.getSalesName());
        existingSales.setProject(sales.getProject());
        existingSales.setAmount(sales.getAmount());
        existingSales.setSalesDate(sales.getSalesDate());
        existingSales.setPaymentStatus(sales.getPaymentStatus());

        salesRepository.save(existingSales);

        return "redirect:/sales";
    }

    @PostMapping("/sales/delete/{id}")
    public String deleteSales(@PathVariable int id) {
        salesRepository.deleteById(id);
        return "redirect:/sales";
    }
}