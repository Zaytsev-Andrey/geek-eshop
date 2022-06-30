package ru.geekbrains.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.geekbrains.dto.ProductDto;
import ru.geekbrains.controller.exception.EntityNotFoundException;
import ru.geekbrains.controller.exception.BadRequestException;
import ru.geekbrains.controller.param.ProductListParam;
import ru.geekbrains.mapper.Mapper;
import ru.geekbrains.persist.Picture;
import ru.geekbrains.persist.Product;
import ru.geekbrains.repository.ProductRepository;
import ru.geekbrains.specification.ProductSpecification;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    private static final int DEFAULT_PAGE_NUMBER = 1;
    private static final int DEFAULT_PAGE_COUNT = 5;

    private final ProductRepository productRepository;

    private final PictureService pictureService;

    private final Mapper<Product, ProductDto> productMapper;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository,
                              PictureService pictureService,
                              Mapper<Product, ProductDto> productMapper) {
        this.productRepository = productRepository;
        this.pictureService = pictureService;
        this.productMapper = productMapper;
    }

    @Override
    public ProductDto findProductById(UUID id) {
        Product product = productRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(id.toString(), "Product not found"));

        return productMapper.toDto(product);
    }

    @Override
    public Page<ProductDto> findProductsWithFilter(ProductListParam listParam) {
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

        String sortField = (listParam.getSortField() != null && !listParam.getSortField().isBlank()) ?
                listParam.getSortField() : "id";

        Sort sort = ("desc".equals(listParam.getSortDirection())) ?
                Sort.by(sortField).descending() : Sort.by(sortField).ascending();

        Page<Product> page = productRepository.findAll(specification, PageRequest.of(
                Optional.ofNullable(listParam.getPage()).orElse(DEFAULT_PAGE_NUMBER) - 1,
                Optional.ofNullable(listParam.getSize()).orElse(DEFAULT_PAGE_COUNT), sort));
        return page
                .map(productMapper::toDto);
    }

    @Override
    @Transactional
    public void saveProduct(ProductDto productDto) {
    	Product product = productMapper.toEntity(productDto);
    	productRepository.findProductById(product.getId()).ifPresent(p -> product.setPictures(p.getPictures()));
    	
        if (productDto.getNewPictures() != null) {
            for (MultipartFile newPicture : productDto.getNewPictures()) {
                try {
                    if (newPicture.getBytes().length == 0) {
                        continue;
                    }
                    product.addPicture(new Picture(
                            newPicture.getOriginalFilename(),
                            newPicture.getContentType(),
                            pictureService.uploadPicture(newPicture.getBytes())));
                } catch (IOException e) {
                    throw new BadRequestException("Upload pictures error");
                }
            }
        }

        productRepository.save(product);
    }

    @Override
    @Transactional
    public void deleteProductById(UUID id) {
        productRepository.deleteById(id);
    }
}
