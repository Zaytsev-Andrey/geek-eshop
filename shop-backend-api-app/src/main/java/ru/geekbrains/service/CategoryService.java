package ru.geekbrains.service;

import ru.geekbrains.controller.dto.CategoryDto;

import java.util.List;

public interface CategoryService {

    List<CategoryDto> findAll();
}
