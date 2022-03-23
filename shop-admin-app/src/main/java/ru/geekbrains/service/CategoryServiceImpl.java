package ru.geekbrains.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.geekbrains.dto.CategoryDto;
import ru.geekbrains.controller.exception.EntityNotFoundException;
import ru.geekbrains.controller.param.CategoryListParam;
import ru.geekbrains.mapper.Mapper;
import ru.geekbrains.persist.Category;
import ru.geekbrains.repository.CategoryRepository;
import ru.geekbrains.specification.CategorySpecification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private static final int DEFAULT_PAGE_NUMBER = 1;
    private static final int DEFAULT_PAGE_COUNT = 5;

    private CategoryRepository categoryRepository;

    private Mapper<Category, CategoryDto> categoryMapper;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, Mapper<Category, CategoryDto> categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public List<CategoryDto> findAllCategories() {
        return categoryRepository.findAll().stream()
                .map(category -> categoryMapper.toDto(category))
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto findCategoryById(UUID id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id.toString(), "Category not found"));
        return categoryMapper.toDto(category);
    }

    @Override
    public Page<CategoryDto> findCategoryWithFilter(CategoryListParam listParam) {
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
                        Optional.ofNullable(listParam.getSize()).orElse(DEFAULT_PAGE_COUNT), sort))
                .map(categoryMapper::toDto);
    }

    @Override
    public void saveCategory(CategoryDto categoryDTO) {
        categoryRepository.save(categoryMapper.toEntity(categoryDTO));
    }

    @Override
    @Transactional
    public void deleteCategoryById(UUID id) {
        categoryRepository.deleteById(id);
    }
}
