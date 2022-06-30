package ru.geekbrains.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.geekbrains.dto.CategoryDto;
import ru.geekbrains.mapper.Mapper;
import ru.geekbrains.persist.Category;
import ru.geekbrains.repository.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final Mapper<Category, CategoryDto> categoryMapper;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, Mapper<Category, CategoryDto> categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public List<CategoryDto> findAll() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }
}
