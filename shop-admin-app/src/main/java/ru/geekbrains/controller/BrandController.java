package ru.geekbrains.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import ru.geekbrains.dto.BrandDto;
import ru.geekbrains.controller.param.BrandListParam;
import ru.geekbrains.service.BrandService;

import java.util.UUID;

import javax.validation.Valid;

@Controller
@RequestMapping("/brand")
public class BrandController {

    private final Logger logger = LoggerFactory.getLogger(BrandController.class);

    private BrandService brandService;

    @Autowired
    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @GetMapping
    public String showBrandListWithPaginationAndFilter(Model model, BrandListParam listParam) {
        logger.info("Getting page of brands with filter");
        model.addAttribute("brandDtos", brandService.findBrandsWithFilter(listParam));
        return "brands";
    }

    @GetMapping("/new")
    public String initNewBrandForm(Model model) {
        logger.info("Initialization form to create new brand");
        model.addAttribute("brandDto", new BrandDto());
        return "brand_form";
    }

    @GetMapping("/{id}")
    public String initEditBrandForm(@PathVariable("id") UUID id, Model model) {
        logger.info("Editing brand with id='{}'", id);
        model.addAttribute("brandDto", brandService.findBrandById(id));
        return "brand_form";
    }

    @PostMapping
    public String saveBrand(@Valid BrandDto brandDto, BindingResult bindingResult) {
        logger.info("Saving brand '{}'", brandDto.getTitle());
        if (bindingResult.hasErrors()) {
            return "brand_form";
        }
        brandService.saveBrand(brandDto);
        return "redirect:/brand";
    }

    @DeleteMapping("/{id}")
    public String deleteBrand(@PathVariable("id") UUID id) {
        logger.info("Deleting brand with id='{}'", id);
        brandService.deleteBrandById(id);
        return "redirect:/brand";
    }

}
