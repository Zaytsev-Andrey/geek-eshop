package ru.geekbrains.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.geekbrains.controller.BrandDto;
import ru.geekbrains.controller.CategoryDto;
import ru.geekbrains.controller.ProductDto;
import ru.geekbrains.controller.ProductListParam;
import ru.geekbrains.persist.*;
import ru.geekbrains.persist.model.Brand;
import ru.geekbrains.persist.model.Category;
import ru.geekbrains.persist.model.Product;

import java.util.Optional;

@Service
public class ProductServiceImp implements ProductService {

    private ProductRepository productRepository;

    @Autowired
    public ProductServiceImp(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public Page<Product> findWithFilter(ProductListParam listParam) {
        Specification<Product> specification = Specification.where(null);

        if (listParam.getTitleFilter() != null && !listParam.getTitleFilter().isBlank()) {
            specification = specification.and(ProductSpecification.titlePrefix(listParam.getTitleFilter()));
        }
        if (listParam.getCategoryFilter() != null && !listParam.getCategoryFilter().isBlank()) {
            specification = specification.and(ProductSpecification.categoryPrefix(listParam.getCategoryFilter()));
        }
        if (listParam.getBrandFilter() != null && !listParam.getBrandFilter().isBlank()) {
            specification = specification.and(ProductSpecification.brandPrefix(listParam.getBrandFilter()));
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
                        Optional.ofNullable(listParam.getSize()).orElse(5), sort));
    }

    @Override
    public void save(ProductDto productDto) {
        CategoryDto categoryDto = productDto.getCategoryDto();
        BrandDto brandDto = productDto.getBrandDto();
        Product product = new Product(productDto.getId(),
                productDto.getTitle(),
                productDto.getCost(),
                productDto.getDescription(),
                new Category(categoryDto.getId(), categoryDto.getTitle()),
                new Brand(brandDto.getId(), brandDto.getTitle()));
        productRepository.save(product);
    }

    @Override
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }
}
