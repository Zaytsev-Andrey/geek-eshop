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
import ru.geekbrains.controller.dto.BrandDto;
import ru.geekbrains.controller.dto.CategoryDto;
import ru.geekbrains.controller.dto.ProductDto;
import ru.geekbrains.controller.exception.NotFoundException;
import ru.geekbrains.controller.param.ProductListParam;
import ru.geekbrains.persist.model.Brand;
import ru.geekbrains.persist.model.Category;
import ru.geekbrains.persist.model.Picture;
import ru.geekbrains.persist.model.Product;
import ru.geekbrains.service.BrandService;
import ru.geekbrains.service.CategoryService;
import ru.geekbrains.service.PictureService;
import ru.geekbrains.service.ProductService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/product")
public class ProductController {

    private final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private ProductService productService;

    private CategoryService categoryService;

    private BrandService brandService;

    @Autowired
    public ProductController(ProductService productService,
                             CategoryService categoryService,
                             BrandService brandService,
                             PictureService pictureService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.brandService = brandService;
    }

    @GetMapping
    public String productList(Model model, ProductListParam listParam) {
        logger.info("Product list page requested");

        Page<Product> products = productService.findWithFilter(listParam);
        model.addAttribute("products", products);
        model.addAttribute("categories", findAllCategoryDto());
        model.addAttribute("brands", findAllBrandDto());
        return "products";
    }

    @GetMapping("/new")
    public String newProductForm(Model model) {
        logger.info("Change product page requested");

        model.addAttribute("productDto", new ProductDto());
        model.addAttribute("categories", findAllCategoryDto());
        model.addAttribute("brands", findAllBrandDto());

        return "product_form";
    }

    @GetMapping("/{id}")
    public String editProduct(@PathVariable("id") Long id, Model model) {
        logger.info("Editing product");

        Product currentProduct = productService.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));
        Category currentCategory= currentProduct.getCategory();
        Brand currentBrand = currentProduct.getBrand();
        List<Picture> currentPictures = currentProduct.getPictures();

        model.addAttribute("productDto", new ProductDto(currentProduct.getId(),
                currentProduct.getTitle(),
                currentProduct.getCost(),
                currentProduct.getDescription(),
                new CategoryDto(currentCategory.getId(), currentCategory.getTitle()),
                new BrandDto(currentBrand.getId(), currentBrand.getTitle()),
                currentPictures.stream().map(Picture::getId).collect(Collectors.toList())
                ));
        model.addAttribute("categories", findAllCategoryDto());
        model.addAttribute("brands", findAllBrandDto());

        return "product_form";
    }

    @PostMapping
    public String update(@Valid ProductDto productDto, BindingResult result, Model model) {
        logger.info("Updating product");

        if (result.hasErrors()) {
            model.addAttribute("categories", findAllCategoryDto());
            model.addAttribute("brands", findAllBrandDto());
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

    private List<CategoryDto> findAllCategoryDto() {
        return categoryService.findAll().stream()
                .map(category -> new CategoryDto(category.getId(), category.getTitle()))
                .collect(Collectors.toList());
    }

    private List<BrandDto> findAllBrandDto() {
        return brandService.findAll().stream()
                .map(brand -> new BrandDto(brand.getId(), brand.getTitle()))
                .collect(Collectors.toList());
    }

    @ExceptionHandler
    public ModelAndView notFoundExceptionHandler(NotFoundException e) {
        ModelAndView modelAndView = new ModelAndView("not_found");
        modelAndView.addObject("message", e.getMessage());
        modelAndView.setStatus(HttpStatus.NOT_FOUND);
        return modelAndView;
    }
}
