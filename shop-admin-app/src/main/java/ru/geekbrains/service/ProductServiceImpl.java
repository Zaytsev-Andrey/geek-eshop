package ru.geekbrains.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.geekbrains.controller.dto.ProductDto;
import ru.geekbrains.controller.exception.NotFoundException;
import ru.geekbrains.controller.param.ProductListParam;
import ru.geekbrains.persist.model.Picture;
import ru.geekbrains.persist.model.Product;
import ru.geekbrains.persist.repository.BrandRepository;
import ru.geekbrains.persist.repository.CategoryRepository;
import ru.geekbrains.persist.repository.ProductRepository;
import ru.geekbrains.persist.specification.ProductSpecification;

import java.io.IOException;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

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
    @Transactional
    public void save(ProductDto productDto) {
        Product product = (productDto.getId() != null) ? productRepository.findById(productDto.getId())
                .orElseThrow(() -> new NotFoundException("Product not found")) : new Product();

        product.setTitle(productDto.getTitle());
        product.setCost(productDto.getCost());
        product.setDescription(productDto.getDescription());
        product.setCategory(categoryRepository.findById(productDto.getCategoryDto().getId())
                .orElseThrow(() -> new NotFoundException("Category not found")));
        product.setBrand(brandRepository.findById(productDto.getBrandDto().getId())
                .orElseThrow(() -> new NotFoundException("Brand not found")));

        if (productDto.getNewPictures() != null) {
            for (MultipartFile newPicture : productDto.getNewPictures()) {
                try {
                    if (newPicture.getBytes().length == 0) {
                        continue;
                    }

                    product.getPictures().add(new Picture(
                            newPicture.getOriginalFilename(),
                            newPicture.getContentType(),
                            pictureService.createPicture(newPicture.getBytes()),
                            product
                    ));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        productRepository.save(product);
    }

    @Override
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }
}
