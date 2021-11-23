package ru.geekbrains.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.geekbrains.controller.dto.BrandDto;
import ru.geekbrains.controller.dto.CategoryDto;
import ru.geekbrains.controller.dto.ProductDto;
import ru.geekbrains.controller.exception.EntityNotFoundException;
import ru.geekbrains.controller.exception.BadRequestException;
import ru.geekbrains.controller.param.ProductListParam;
import ru.geekbrains.persist.model.Picture;
import ru.geekbrains.persist.model.Product;
import ru.geekbrains.persist.repository.BrandRepository;
import ru.geekbrains.persist.repository.CategoryRepository;
import ru.geekbrains.persist.repository.ProductRepository;
import ru.geekbrains.persist.specification.ProductSpecification;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    private static final int DEFAULT_PAGE_NUMBER = 1;
    private static final int DEFAULT_PAGE_COUNT = 5;

    private ProductRepository productRepository;

    private CategoryRepository categoryRepository;

    private BrandRepository brandRepository;

    private PictureService pictureService;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository,
                              CategoryRepository categoryRepository,
                              BrandRepository brandRepository,
                              PictureService pictureService) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.brandRepository = brandRepository;
        this.pictureService = pictureService;
    }

    @Override
    public ProductDto findProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(id, "Product not found"));

        return new ProductDto(
                product.getId(),
                product.getTitle(),
                product.getCost(),
                product.getDescription(),
                new CategoryDto(product.getCategory().getId(), product.getCategory().getTitle()),
                new BrandDto(product.getBrand().getId(), product.getBrand().getTitle()),
                product.getPictures().stream()
                        .map(Picture::getId)
                        .collect(Collectors.toList())
        );
    }

    @Override
    public Page<Product> findProductsWithFilter(ProductListParam listParam) {
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

        return productRepository.findAll(specification, PageRequest.of(
                Optional.ofNullable(listParam.getPage()).orElse(DEFAULT_PAGE_NUMBER) - 1,
                Optional.ofNullable(listParam.getSize()).orElse(DEFAULT_PAGE_COUNT),
                sort));
    }

    @Override
    @Transactional
    public void saveProduct(ProductDto productDto) {
        Product product = (productDto.getId() != null) ? productRepository.findById(productDto.getId())
                .orElseThrow(() ->
                        new EntityNotFoundException(productDto.getId(), "Product not found"))
                : new Product();

        product.setTitle(productDto.getTitle());
        product.setCost(productDto.getCost());
        product.setDescription(productDto.getDescription());
        product.setCategory(categoryRepository.findById(productDto.getCategoryDto().getId())
                .orElseThrow(() ->
                        new EntityNotFoundException(productDto.getCategoryDto().getId(), "Category not found")));
        product.setBrand(brandRepository.findById(productDto.getBrandDto().getId())
                .orElseThrow(() ->
                        new EntityNotFoundException(productDto.getBrandDto().getId(), "Brand not found")));

        if (productDto.getNewPictures() != null) {
            for (MultipartFile newPicture : productDto.getNewPictures()) {
                try {
                    if (newPicture.getBytes().length == 0) {
                        continue;
                    }
                    product.addPicture(new Picture(
                            newPicture.getOriginalFilename(),
                            newPicture.getContentType(),
                            pictureService.savePicture(newPicture.getBytes())));
                } catch (IOException e) {
                    throw new BadRequestException("Upload pictures error");
                }
            }
        }

        productRepository.save(product);
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.deleteById(id);
    }
}
