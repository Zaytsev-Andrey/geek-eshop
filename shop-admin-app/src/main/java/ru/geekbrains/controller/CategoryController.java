package ru.geekbrains.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.geekbrains.persist.model.Category;
import ru.geekbrains.service.CategoryService;

import javax.validation.Valid;

@Controller
@RequestMapping("/category")
public class CategoryController {

    private final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    private CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String categoryList(Model model, CategoryListParam listParam) {
        logger.info("Category list page requested");

        model.addAttribute("categories", categoryService.findWithFilter(listParam));
        return "categories";
    }

    @GetMapping("/new")
    public String newProductForm(Model model) {
        logger.info("Change category page requested");

        model.addAttribute("categoryDto", new CategoryDto());
        return "category_form";
    }

    @GetMapping("/{id}")
    public String editCategory(@PathVariable("id") Long id, Model model) {
        logger.info("Editing category");

        Category currentCategory = categoryService.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found"));
        model.addAttribute("categoryDto", new CategoryDto(currentCategory.getId(), currentCategory.getTitle()));

        return "category_form";
    }

    @PostMapping
    public String update(@Valid CategoryDto categoryDTO, BindingResult bindingResult) {
        logger.info("Updating category");

        if (bindingResult.hasErrors()) {
            return "category_form";
        }

        categoryService.save(categoryDTO);
        return "redirect:/category";
    }

    @DeleteMapping("/{id}")
    public String removeProduct(@PathVariable("id") Long id) {
        logger.info("Deleting product");

        categoryService.deleteById(id);

        return "redirect:/category";
    }

    @ExceptionHandler
    public ModelAndView notFoundExceptionHandler(NotFoundException e) {
        ModelAndView modelAndView = new ModelAndView("not_found");
        modelAndView.addObject("message", e.getMessage());
        modelAndView.setStatus(HttpStatus.NOT_FOUND);
        return modelAndView;
    }
}
