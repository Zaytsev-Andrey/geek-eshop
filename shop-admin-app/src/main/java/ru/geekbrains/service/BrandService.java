package ru.geekbrains.service;

import org.springframework.data.domain.Page;
import ru.geekbrains.controller.BrandDto;
import ru.geekbrains.controller.BrandListParam;
import ru.geekbrains.controller.CategoryDto;
import ru.geekbrains.persist.model.Brand;
import ru.geekbrains.persist.model.Category;

import java.util.List;
import java.util.Optional;

public interface BrandService {

    List<Brand> findAll();

    Optional<Brand> findById(Long id);

    Page<Brand> findWithFilter(BrandListParam listParam);

    void save(BrandDto brandDto);

    void deleteById(Long id);
}
