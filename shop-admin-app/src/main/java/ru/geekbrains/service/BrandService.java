package ru.geekbrains.service;

import org.springframework.data.domain.Page;
import ru.geekbrains.controller.dto.BrandDto;
import ru.geekbrains.controller.param.BrandListParam;
import ru.geekbrains.persist.model.Brand;

import java.util.List;
import java.util.Optional;

public interface BrandService {

    List<BrandDto> findAllBrands();

    BrandDto findBrandById(Long id);

    Page<Brand> findBrandsWithFilter(BrandListParam listParam);

    void saveBrand(BrandDto brandDto);

    void deleteBrandById(Long id);
}
