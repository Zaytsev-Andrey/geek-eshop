package ru.geekbrains.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ru.geekbrains.dto.ProductDto;
import ru.geekbrains.controller.param.ProductListParam;
import ru.geekbrains.service.ProductService;

import java.util.UUID;

@RestController
@RequestMapping("/product")
public class ProductController {

    private ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/all")
    public Page<ProductDto> showProductListWithPaginationAndFilter(ProductListParam listParam) {
        return productService.findWithFilter(listParam);
    }

    @GetMapping("/{id}")
    public ProductDto showProductForm(@PathVariable("id") UUID id) {

        return productService.findById(id);
    }
}
