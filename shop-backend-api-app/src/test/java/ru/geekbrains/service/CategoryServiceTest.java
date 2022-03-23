package ru.geekbrains.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.geekbrains.controller.dto.CategoryDto;
import ru.geekbrains.persist.Category;
import ru.geekbrains.repository.CategoryRepository;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.*;

public class CategoryServiceTest {

    private CategoryService categoryService;

    private CategoryRepository categoryRepository;

    @BeforeEach
    public void init() {
        categoryRepository = mock(CategoryRepository.class);
        categoryService = new CategoryServiceImpl(categoryRepository);
    }

    @Test
    public void testFindAll() {
        Category categoryMonitor = new Category(1L, "Monitor");
        Category categoryLaptop = new Category(2L, "Laptop");

        when(categoryRepository.findAll()).thenReturn(List.of(categoryMonitor, categoryLaptop));

        List<CategoryDto> dtoCategories = categoryService.findAll();

        assertNotNull(dtoCategories);
        assertEquals(2, dtoCategories.size());
        assertEquals(categoryMonitor.getId(), dtoCategories.get(0).getId());
        assertEquals(categoryMonitor.getTitle(), dtoCategories.get(0).getTitle());
        assertEquals(categoryLaptop.getId(), dtoCategories.get(1).getId());
        assertEquals(categoryLaptop.getTitle(), dtoCategories.get(1).getTitle());
    }
}
