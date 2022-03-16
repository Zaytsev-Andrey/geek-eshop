package ru.geekbrains.service;

import org.springframework.data.domain.Page;
import ru.geekbrains.dto.CategoryDto;
import ru.geekbrains.controller.param.CategoryListParam;
import ru.geekbrains.persist.model.Category;

import java.util.List;
import java.util.UUID;

public interface CategoryService {

    List<CategoryDto> findAllCategories();

    CategoryDto findCategoryById(UUID id);

    Page<Category> findCategoryWithFilter(CategoryListParam listParam);

    void saveCategory(CategoryDto categoryDTO);

    void deleteCategoryById(UUID id);
}
