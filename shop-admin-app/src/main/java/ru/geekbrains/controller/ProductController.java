package ru.geekbrains.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.geekbrains.dto.BrandDto;
import ru.geekbrains.dto.CategoryDto;
import ru.geekbrains.dto.ProductDto;
import ru.geekbrains.controller.param.ProductListParam;
import ru.geekbrains.service.BrandService;
import ru.geekbrains.service.CategoryService;
import ru.geekbrains.service.ProductService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/product")
public class ProductController {

    private final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    private final CategoryService categoryService;

    private final BrandService brandService;

    @Autowired
    public ProductController(ProductService productService,
                             CategoryService categoryService,
                             BrandService brandService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.brandService = brandService;
    }

    /**
     * Load before each request method for loading list of categories to model
     * @return list of product categories
     */
    @ModelAttribute("categories")
    public List<CategoryDto> loadCategories() {
        logger.info("Loading categories to model");
        return categoryService.findAllCategories();
    }

    /**
     * Load before each request method for loading list of brands to model
     * @return list of product brands
     */
    @ModelAttribute("brands")
    public List<BrandDto> loadBrands() {
        logger.info("Loading brands to model");
        return brandService.findAllBrands();
    }

    @GetMapping
    public String showProductListWithPaginationAndFilter(Model model, ProductListParam listParam) {
        logger.info("Getting page of products with filter");
        Page<ProductDto> productDtos = productService.findProductsWithFilter(listParam);
        model.addAttribute("productDtos", productDtos);
        return "products";
    }

    @GetMapping("/new")
    public String initNewProductForm(Model model) {
        logger.info("Initialization form to create new product");
        model.addAttribute("productDto", new ProductDto());
        return "product_form";
    }

    @GetMapping("/{id}")
    public String initEditProductForm(@PathVariable("id") UUID id, Model model) {
        logger.info("Editing product with id='{}'", id);
        model.addAttribute("productDto", productService.findProductById(id));
        return "product_form";
    }

    @PostMapping
    public String saveProduct(@Valid ProductDto productDto, BindingResult result) {
        logger.info("Saving product '{}'", productDto.getTitle());
        if (result.hasErrors()) {
            return "product_form";
        }
        productService.saveProduct(productDto);
        return "redirect:/product";
    }

    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable("id") UUID id) {
        logger.info("Deleting product with id='{}'", id);
        productService.deleteProductById(id);
        return "redirect:/product";
    }

}
