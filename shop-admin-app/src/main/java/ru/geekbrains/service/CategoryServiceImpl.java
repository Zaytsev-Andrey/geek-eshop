package ru.geekbrains.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.geekbrains.controller.dto.CategoryDto;
import ru.geekbrains.controller.exception.BadRequestException;
import ru.geekbrains.controller.exception.EntityNotFoundException;
import ru.geekbrains.controller.param.CategoryListParam;
import ru.geekbrains.persist.model.Category;
import ru.geekbrains.persist.repository.CategoryRepository;
import ru.geekbrains.persist.specification.CategorySpecification;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private static final int DEFAULT_PAGE_NUMBER = 1;
    private static final int DEFAULT_PAGE_COUNT = 5;

    private CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<CategoryDto> findAllCategories() {
        return categoryRepository.findAll().stream()
                .map(category -> new CategoryDto(category.getId(), category.getTitle()))
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto findCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, "Category not found"));
        return new CategoryDto(category.getId(), category.getTitle());
    }

    @Override
    public Page<Category> findCategoryWithFilter(CategoryListParam listParam) {
        Specification<Category> specification = Specification.where(null);

        if (listParam.getTitleFilter() != null && !listParam.getTitleFilter().isBlank()) {
            specification = specification.and(CategorySpecification.titlePrefix(listParam.getTitleFilter()));
        }

        String sortField = (listParam.getSortField() != null && !listParam.getSortField().isBlank()) ?
                listParam.getSortField() : "id";

        Sort sort = ("desc".equals(listParam.getSortDirection())) ?
                Sort.by(sortField).descending() : Sort.by(sortField).ascending();

        return categoryRepository.findAll(specification, PageRequest.of(
                Optional.ofNullable(listParam.getPage()).orElse(DEFAULT_PAGE_NUMBER) - 1,
                Optional.ofNullable(listParam.getSize()).orElse(DEFAULT_PAGE_COUNT),
                sort));
    }

    @Override
    public void saveCategory(CategoryDto categoryDTO) {
        Category category = new Category(categoryDTO.getId(), categoryDTO.getTitle());
        categoryRepository.save(category);
    }

    @Override
    public void deleteCategoryById(Long id) {
        categoryRepository.deleteById(id);
    }
}
