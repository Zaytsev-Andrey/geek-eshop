package ru.geekbrains.service;

import org.springframework.data.domain.Page;
import ru.geekbrains.controller.dto.BrandDto;
import ru.geekbrains.controller.param.BrandListParam;
import ru.geekbrains.persist.model.Brand;

import java.util.List;
import java.util.Optional;

public interface BrandService {

    List<Brand> findAll();

    Optional<Brand> findById(Long id);

    Page<Brand> findWithFilter(BrandListParam listParam);

    void save(BrandDto brandDto);

    void deleteById(Long id);
}
