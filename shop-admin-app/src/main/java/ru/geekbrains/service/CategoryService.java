package ru.geekbrains.service;

import org.springframework.data.domain.Page;
import ru.geekbrains.controller.CategoryDto;
import ru.geekbrains.controller.CategoryListParam;
import ru.geekbrains.persist.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    List<Category> findAll();

    Optional<Category> findById(Long id);

    Page<Category> findWithFilter(CategoryListParam listParam);

    void save(CategoryDto categoryDTO);

    void deleteById(Long id);
}
