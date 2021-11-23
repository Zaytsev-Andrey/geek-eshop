package ru.geekbrains.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.geekbrains.controller.dto.BrandDto;
import ru.geekbrains.controller.exception.EntityNotFoundException;
import ru.geekbrains.controller.param.BrandListParam;
import ru.geekbrains.persist.repository.BrandRepository;
import ru.geekbrains.persist.specification.BrandSpecification;
import ru.geekbrains.persist.model.Brand;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BrandServiceImpl implements BrandService {

    private static final int DEFAULT_PAGE_NUMBER = 1;
    private static final int DEFAULT_PAGE_COUNT = 5;

    private BrandRepository brandRepository;

    @Autowired
    public BrandServiceImpl(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Override
    public List<BrandDto> findAllBrands() {
        return brandRepository.findAll().stream()
                .map(brand -> new BrandDto(brand.getId(), brand.getTitle()))
                .collect(Collectors.toList());
    }

    @Override
    public BrandDto findBrandById(Long id) {
        Brand brand = brandRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(id, "Brand not found"));
        return new BrandDto(brand.getId(), brand.getTitle());
    }

    @Override
    public Page<Brand> findBrandsWithFilter(BrandListParam listParam) {
        Specification<Brand> specification = Specification.where(null);

        if (listParam.getTitleFilter() != null && !listParam.getTitleFilter().isBlank()) {
            specification = specification.and(BrandSpecification.titlePrefix(listParam.getTitleFilter()));
        }

        String sortField = (listParam.getSortField() != null && !listParam.getSortField().isBlank()) ?
                listParam.getSortField() : "id";

        Sort sort = ("desc".equals(listParam.getSortDirection())) ?
                Sort.by(sortField).descending() : Sort.by(sortField).ascending();


        return brandRepository.findAll(specification, PageRequest.of(
                Optional.ofNullable(listParam.getPage()).orElse(DEFAULT_PAGE_NUMBER) - 1,
                Optional.ofNullable(listParam.getSize()).orElse(DEFAULT_PAGE_COUNT),
                sort));
    }

    @Override
    public void saveBrand(BrandDto brandDto) {
        brandRepository.save(new Brand(brandDto.getId(), brandDto.getTitle()));
    }

    @Override
    public void deleteBrandById(Long id) {
        brandRepository.deleteById(id);
    }
}
