package ru.geekbrains.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.geekbrains.controller.dto.BrandDto;
import ru.geekbrains.controller.dto.CategoryDto;
import ru.geekbrains.controller.dto.ProductDto;
import ru.geekbrains.controller.param.ProductListParam;
import ru.geekbrains.persist.model.Picture;
import ru.geekbrains.persist.model.Product;
import ru.geekbrains.persist.repository.ProductRepository;
import ru.geekbrains.persist.specification.ProductSpecification;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Optional<ProductDto> findById(Long id) {
        return productRepository.findById(id)
                .map(product -> new ProductDto(product.getId(),
                        product.getTitle(),
                        product.getCost(),
                        product.getDescription(),
                        new CategoryDto(product.getCategory().getId(), product.getCategory().getTitle()),
                        new BrandDto(product.getBrand().getId(), product.getBrand().getTitle()),
                        product.getPictures().stream()
                                .map(Picture::getId)
                                .collect(Collectors.toList())
                        ));
    }

    @Override
    public Page<ProductDto> findWithFilter(ProductListParam listParam) {
        Specification<Product> specification = Specification.where(null);

        if (listParam.getTitleFilter() != null && !listParam.getTitleFilter().isBlank()) {
            specification = specification.and(ProductSpecification.titlePrefix(listParam.getTitleFilter()));
        }
        if (listParam.getCategoryFilter() != null && listParam.getCategoryFilter() > 0) {
            specification = specification.and(ProductSpecification.categoryId(listParam.getCategoryFilter()));
        }
        if (listParam.getBrandFilter() != null && listParam.getBrandFilter() > 0) {
            specification = specification.and(ProductSpecification.brandId(listParam.getBrandFilter()));
        }
        if (listParam.getMinCostFilter() != null) {
            specification = specification.and(ProductSpecification.minCost(listParam.getMinCostFilter()));
        }
        if (listParam.getMaxCostFilter() != null) {
            specification = specification.and(ProductSpecification.maxCost(listParam.getMaxCostFilter()));
        }

        String sortField = "id";
        if (listParam.getSortField() != null && !listParam.getSortField().isBlank()) {
            sortField = listParam.getSortField();
        }

        Sort sort = Sort.by(sortField).ascending();
        if ("desc".equals(listParam.getSortDirection())) {
            sort = sort.descending();
        }

        return productRepository.findAll(specification,
                PageRequest.of(Optional.ofNullable(listParam.getPage()).orElse(1) - 1,
                        Optional.ofNullable(listParam.getSize()).orElse(5), sort))
                .map(product -> new ProductDto(
                        product.getId(),
                        product.getTitle(),
                        product.getCost(),
                        product.getDescription(),
                        new CategoryDto(product.getCategory().getId(), product.getCategory().getTitle()),
                        new BrandDto(product.getBrand().getId(), product.getBrand().getTitle()),
                        product.getPictures().stream()
                                .map(Picture::getId)
                                .collect(Collectors.toList())
                ));
    }

}
