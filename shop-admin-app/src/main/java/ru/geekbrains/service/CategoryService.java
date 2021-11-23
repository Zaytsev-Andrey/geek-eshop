package ru.geekbrains.service;

import org.springframework.data.domain.Page;
import ru.geekbrains.controller.dto.CategoryDto;
import ru.geekbrains.controller.param.CategoryListParam;
import ru.geekbrains.persist.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    List<CategoryDto> findAllCategories();

    CategoryDto findCategoryById(Long id);

    Page<Category> findCategoryWithFilter(CategoryListParam listParam);

    void saveCategory(CategoryDto categoryDTO);

    void deleteCategoryById(Long id);
}
