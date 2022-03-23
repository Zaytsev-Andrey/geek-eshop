package ru.geekbrains.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.geekbrains.dto.ProductDto;
import ru.geekbrains.controller.param.ProductListParam;
import ru.geekbrains.exception.ProductNotFoundException;
import ru.geekbrains.mapper.Mapper;
import ru.geekbrains.persist.Product;
import ru.geekbrains.repository.ProductRepository;
import ru.geekbrains.specification.ProductSpecification;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;

    private Mapper<Product, ProductDto> productMapper;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, Mapper<Product, ProductDto> productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    public ProductDto findById(UUID id) {
        return productRepository.findById(id)
                .map(productMapper::toDto)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    public Page<ProductDto> findWithFilter(ProductListParam listParam) {
        Specification<Product> specification = Specification.where(null);

        if (listParam.getTitleFilter() != null && !listParam.getTitleFilter().isBlank()) {
            specification = specification.and(ProductSpecification.titleLike(listParam.getTitleFilter()));
        }
        if (listParam.getCategoriesFilter() != null && listParam.getCategoriesFilter().size() > 0) {
            specification = specification.and(ProductSpecification.categoryContains(listParam.getCategoriesFilter().stream()
            		.map(id -> UUID.fromString(id))
            		.collect(Collectors.toList())));
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
                .map(productMapper::toDto);
    }

	@Override
	public List<Product> findProductByIdIn(Set<UUID> ids) {
		// TODO Auto-generated method stub
		return productRepository.findProductByIdIn(ids);
	}
    
    

}
