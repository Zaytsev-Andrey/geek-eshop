package ru.geekbrains.service;

import org.springframework.data.domain.Page;
import ru.geekbrains.dto.BrandDto;
import ru.geekbrains.controller.param.BrandListParam;
import ru.geekbrains.persist.model.Brand;

import java.util.List;
import java.util.UUID;

public interface BrandService {

    List<BrandDto> findAllBrands();

    BrandDto findBrandById(UUID id);

    Page<Brand> findBrandsWithFilter(BrandListParam listParam);

    void saveBrand(BrandDto brandDto);

    void deleteBrandById(UUID id);
}
