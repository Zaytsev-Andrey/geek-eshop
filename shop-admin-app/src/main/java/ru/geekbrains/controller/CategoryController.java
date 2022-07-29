package ru.geekbrains.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.geekbrains.dto.CategoryDto;
import ru.geekbrains.controller.param.CategoryListParam;
import ru.geekbrains.service.CategoryService;

import java.util.UUID;

import javax.validation.Valid;

@Controller
@RequestMapping("/category")
public class CategoryController {

    private final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String showCategoryListWithPaginationAndFilter(Model model, CategoryListParam listParam) {
        logger.info("Getting page of categories with filter");
        model.addAttribute("categoryDtos", categoryService.findCategoryWithFilter(listParam));
        return "categories";
    }

    @GetMapping("/new")
    public String initNewCategoryForm(Model model) {
        logger.info("Initialization form to create new category");
        model.addAttribute("categoryDto", new CategoryDto());
        return "category_form";
    }

    @GetMapping("/{id}")
    public String intEditCategoryForm(@PathVariable("id") UUID id, Model model) {
        logger.info("Editing category with id='{}'", id);
        model.addAttribute("categoryDto", categoryService.findCategoryById(id));
        return "category_form";
    }

    @PostMapping
    public String saveCategory(@Valid CategoryDto categoryDTO, BindingResult bindingResult) {
        logger.info("Saving category '{}'", categoryDTO.getTitle());
        if (bindingResult.hasErrors()) {
            return "category_form";
        }
        categoryService.saveCategory(categoryDTO);
        return "redirect:/category";
    }

    @DeleteMapping("/{id}")
    public String deleteCategory(@PathVariable("id") UUID id) {
        logger.info("Deleting category with id='{}'", id);
        categoryService.deleteCategoryById(id);
        return "redirect:/category";
    }

}
