package ru.geekbrains.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.geekbrains.controller.dto.ProductDto;
import ru.geekbrains.controller.param.ProductListParam;
import ru.geekbrains.service.ProductService;

import java.util.Optional;

@RestController
@RequestMapping("/product")
public class ProductController {

    private ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/all")
    public Page<ProductDto> findAll(ProductListParam listParam) {
        return productService.findWithFilter(listParam);
    }

    @GetMapping("/{id}")
    public Optional<ProductDto> findById(@PathVariable("id") Long id) {

        return productService.findById(id);
    }
}
