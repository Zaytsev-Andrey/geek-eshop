package ru.geekbrains.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import ru.geekbrains.dto.BrandDto;
import ru.geekbrains.controller.exception.EntityNotFoundException;
import ru.geekbrains.controller.param.BrandListParam;
import ru.geekbrains.mapper.Mapper;
import ru.geekbrains.repository.BrandRepository;
import ru.geekbrains.specification.BrandSpecification;
import ru.geekbrains.persist.Brand;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BrandServiceImpl implements BrandService {

	private static final Logger logger = LoggerFactory.getLogger(BrandServiceImpl.class);

    private static final int DEFAULT_PAGE_NUMBER = 1;
    private static final int DEFAULT_PAGE_COUNT = 5;

    private final BrandRepository brandRepository;

    private final Mapper<Brand, BrandDto> brandMapper;

    @Autowired
    public BrandServiceImpl(BrandRepository brandRepository, Mapper<Brand, BrandDto> brandMapper) {
        this.brandRepository = brandRepository;
        this.brandMapper = brandMapper;
    }

    @Override
    public List<BrandDto> findAllBrands() {
        return brandRepository.findAll().stream()
                .map(brandMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public BrandDto findBrandById(UUID id) {
    	logger.info("ID: {}", id);
        Brand brand = brandRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(id.toString(), "Brand not found"));
        return brandMapper.toDto(brand);
    }

    @Override
    public Page<BrandDto> findBrandsWithFilter(BrandListParam listParam) {
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
                         Optional.ofNullable(listParam.getSize()).orElse(DEFAULT_PAGE_COUNT), sort))
                 .map(brandMapper::toDto);
    }

    @Override
    public void saveBrand(BrandDto brandDto) {
        brandRepository.save(brandMapper.toEntity(brandDto));
    }

    @Override
    public void deleteBrandById(UUID id) {
        brandRepository.deleteById(id);
    }
}
