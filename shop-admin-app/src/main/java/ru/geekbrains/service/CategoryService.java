package ru.geekbrains.service;

import org.springframework.data.domain.Page;
import ru.geekbrains.controller.dto.CategoryDto;
import ru.geekbrains.controller.param.CategoryListParam;
import ru.geekbrains.persist.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    List<Category> findAll();

    Optional<Category> findById(Long id);

    Page<Category> findWithFilter(CategoryListParam listParam);

    void save(CategoryDto categoryDTO);

    void deleteById(Long id);
}
