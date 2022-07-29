package ru.geekbrains.service;


import java.util.List;

import ru.geekbrains.dto.CategoryDto;

public interface CategoryService {

    List<CategoryDto> findAll();
}
