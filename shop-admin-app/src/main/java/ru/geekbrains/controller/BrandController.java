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
import ru.geekbrains.controller.dto.BrandDto;
import ru.geekbrains.controller.exception.NotFoundException;
import ru.geekbrains.controller.param.BrandListParam;
import ru.geekbrains.persist.model.Brand;
import ru.geekbrains.service.BrandService;

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
    public String brandList(Model model, BrandListParam listParam) {
        logger.info("Brand list page requested");

        model.addAttribute("brands", brandService.findWithFilter(listParam));
        return "brands";
    }

    @GetMapping("/new")
    public String newBrandForm(Model model) {
        logger.info("Change brand page requested");

        model.addAttribute("brandDto", new BrandDto());
        return "brand_form";
    }

    @GetMapping("/{id}")
    public String editBrand(@PathVariable("id") Long id, Model model) {
        logger.info("Editing brand");

        Brand currentBrand = brandService.findById(id)
                .orElseThrow(() -> new NotFoundException("Brand not found"));
        model.addAttribute("brandDto", new BrandDto(currentBrand.getId(), currentBrand.getTitle()));

        return "brand_form";
    }

    @PostMapping
    public String update(@Valid BrandDto brandDto, BindingResult bindingResult) {
        logger.info("Updating brand");

        if (bindingResult.hasErrors()) {
            return "brand_form";
        }

        brandService.save(brandDto);
        return "redirect:/brand";
    }

    @DeleteMapping("/{id}")
    public String removeBrand(@PathVariable("id") Long id) {
        logger.info("Deleting brand");

        brandService.deleteById(id);

        return "redirect:/brand";
    }

    @ExceptionHandler
    public ModelAndView notFoundExceptionHandler(NotFoundException e) {
        ModelAndView modelAndView = new ModelAndView("not_found");
        modelAndView.addObject("message", e.getMessage());
        modelAndView.setStatus(HttpStatus.NOT_FOUND);
        return modelAndView;
    }
}
