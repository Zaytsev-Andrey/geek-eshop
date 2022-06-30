package ru.geekbrains.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import ru.geekbrains.dto.CategoryDto;
import ru.geekbrains.mapper.IdUUIDMapper;
import ru.geekbrains.persist.Category;
import ru.geekbrains.repository.CategoryRepository;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.*;

public class CategoryServiceTest {

    private CategoryService categoryService;

    private CategoryRepository categoryRepository;

    @BeforeEach
    public void init() {
        categoryRepository = mock(CategoryRepository.class);
        IdUUIDMapper<Category, CategoryDto> mapper = new IdUUIDMapper<>(new ModelMapper(), Category.class, CategoryDto.class);
        mapper.initMapper();
        categoryService = new CategoryServiceImpl(categoryRepository, mapper);
    }

    @Test
    public void testFindAll() {
        Category categoryMonitor = new Category(UUID.randomUUID(), "Monitor");
        Category categoryLaptop = new Category(UUID.randomUUID(), "Laptop");

        when(categoryRepository.findAll()).thenReturn(List.of(categoryMonitor, categoryLaptop));

        List<CategoryDto> dtoCategories = categoryService.findAll();

        assertNotNull(dtoCategories);
        assertEquals(2, dtoCategories.size());
        assertEquals(categoryMonitor.getId().toString(), dtoCategories.get(0).getId());
        assertEquals(categoryMonitor.getTitle(), dtoCategories.get(0).getTitle());
        assertEquals(categoryLaptop.getId().toString(), dtoCategories.get(1).getId());
        assertEquals(categoryLaptop.getTitle(), dtoCategories.get(1).getTitle());
    }
}
