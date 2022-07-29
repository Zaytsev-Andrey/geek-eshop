package ru.geekbrains.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import ru.geekbrains.controller.param.ProductListParam;
import ru.geekbrains.dto.BrandDto;
import ru.geekbrains.dto.CategoryDto;
import ru.geekbrains.dto.ProductDto;
import ru.geekbrains.mapper.IdUUIDMapper;
import ru.geekbrains.mapper.ProductMapper;
import ru.geekbrains.persist.Brand;
import ru.geekbrains.persist.Category;
import ru.geekbrains.persist.Picture;
import ru.geekbrains.persist.Product;
import ru.geekbrains.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.*;

public class ProductServiceTest {

    private ProductService productService;

    private ProductRepository productRepository;

    @BeforeEach
    public void init() {
        ModelMapper modelMapper = new ModelMapper();
        IdUUIDMapper<Category, CategoryDto> categoryMapper =
                new IdUUIDMapper<>(modelMapper, Category.class, CategoryDto.class);
        categoryMapper.initMapper();
        IdUUIDMapper<Brand, BrandDto> brandMapper = new IdUUIDMapper<>(modelMapper, Brand.class, BrandDto.class);
        brandMapper.initMapper();
        ProductMapper<Product, ProductDto> productMapper =
                new ProductMapper<>(modelMapper, Product.class, ProductDto.class, brandMapper, categoryMapper);
        productMapper.initMapper();

        productRepository = mock(ProductRepository.class);
        productService = new ProductServiceImpl(productRepository, productMapper);
    }

    @Test
    public void testFindById() {
        Product expectedProduct = new Product(
                UUID.randomUUID(),
                "LG 27UP850",
                new BigDecimal(500),
                "4k Monitor",
                new Category(UUID.randomUUID(), "Monitor"),
                new Brand(UUID.randomUUID(), "LG")
        );

        Picture pic1 = new Picture(
                UUID.randomUUID(), "pic1.jpg", "", UUID.randomUUID().toString(), expectedProduct
        );
        Picture pic2 = new Picture(
                UUID.randomUUID(), "pic2.jpg", "", UUID.randomUUID().toString(), expectedProduct
        );
        expectedProduct.setPictures(Set.of(pic1, pic2));


        when(productRepository.findById(eq(expectedProduct.getId())))
                .thenReturn(Optional.of(expectedProduct));

        ProductDto productDto = productService.findById(expectedProduct.getId());

        assertEquals(expectedProduct.getId().toString(), productDto.getId());
        assertEquals(expectedProduct.getTitle(), productDto.getTitle());
        assertEquals(expectedProduct.getCost().toString(), productDto.getCost());
        assertEquals(expectedProduct.getDescription(), productDto.getDescription());
        assertEquals(expectedProduct.getCategory().getId().toString(), productDto.getCategoryDto().getId());
        assertEquals(expectedProduct.getCategory().getTitle(), productDto.getCategoryDto().getTitle());
        assertEquals(expectedProduct.getBrand().getId().toString(), productDto.getBrandDto().getId());
        assertEquals(expectedProduct.getBrand().getTitle(), productDto.getBrandDto().getTitle());

        assertNotNull(productDto.getPictures());
        assertEquals(2, productDto.getPictures().size());
    }

    @Test
    public void testFindWithFilter() {
        Product expectedProduct1 = new Product(
                UUID.randomUUID(),
                "LG 27UP850",
                new BigDecimal(500),
                "4k Monitor",
                new Category(UUID.randomUUID(), "Monitor"),
                new Brand(UUID.randomUUID(), "LG")
        );
        Product expectedProduct2 = new Product(
                UUID.randomUUID(),
                "MacBook Pro 13",
                new BigDecimal(1300),
                "Laptop",
                new Category(UUID.randomUUID(), "Laptop"),
                new Brand(UUID.randomUUID(), "Apple")
        );

        Page<Product> page = new PageImpl<>(List.of(expectedProduct1, expectedProduct2));
        when(productRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(page);

        ProductListParam param = new ProductListParam();
        Page<ProductDto> productDtoPage = productService.findWithFilter(param);
        List<ProductDto> dtoProducts = productDtoPage.get().collect(Collectors.toList());
        assertNotNull(dtoProducts);
        assertEquals(2, dtoProducts.size());
    }
}
