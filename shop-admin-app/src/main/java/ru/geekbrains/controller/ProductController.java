package ru.geekbrains.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.geekbrains.persist.Product;
import ru.geekbrains.service.CategoryService;
import ru.geekbrains.service.ProductService;

import javax.validation.Valid;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/product")
public class ProductController {

    private final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private ProductService productService;

    private CategoryService categoryService;

    @Autowired
    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String productList(Model model, ProductListParam listParam) {
        logger.info("Product list page requested");

        Page<Product> products = productService.findWithFilter(listParam);
        model.addAttribute("products", products);
        return "products";
    }

    @GetMapping("/new")
    public String newProductForm(Model model) {
        logger.info("Change product page requested");

        model.addAttribute("productDto", new ProductDto());
        model.addAttribute("categories", categoryService.findAll().stream()
                .map(category -> new CategoryDto(category.getId(), category.getTitle()))
                .collect(Collectors.toSet()));

        return "product_form";
    }

    @GetMapping("/{id}")
    public String editProduct(@PathVariable("id") Long id, Model model) {
        logger.info("Editing product");

        Product currentProduct = productService.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));
        model.addAttribute("productDto", new ProductDto(currentProduct.getId(),
                currentProduct.getTitle(),
                currentProduct.getCost(),
                currentProduct.getDescription(),
                new CategoryDto(currentProduct.getCategory().getId(), currentProduct.getCategory().getTitle())));
        model.addAttribute("categories", categoryService.findAll().stream()
                .map(category -> new CategoryDto(category.getId(), category.getTitle()))
                .collect(Collectors.toSet()));

        return "product_form";
    }

    @PostMapping
    public String update(@Valid ProductDto productDto, BindingResult result) {
        logger.info("Updating product");

        if (result.hasErrors()) {
            return "product_form";
        }

        productService.save(productDto);
        return "redirect:/product";
    }

    @DeleteMapping("/{id}")
    public String removeProduct(@PathVariable("id") Long id) {
        logger.info("Deleting product");

        productService.deleteById(id);

        return "redirect:/product";
    }

    @ExceptionHandler
    public ModelAndView notFoundExceptionHandler(NotFoundException e) {
        ModelAndView modelAndView = new ModelAndView("not_found");
        modelAndView.addObject("message", e.getMessage());
        modelAndView.setStatus(HttpStatus.NOT_FOUND);
        return modelAndView;
    }
}
