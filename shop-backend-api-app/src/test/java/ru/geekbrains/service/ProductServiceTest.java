package ru.geekbrains.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import ru.geekbrains.controller.dto.ProductDto;
import ru.geekbrains.controller.param.ProductListParam;
import ru.geekbrains.persist.model.Brand;
import ru.geekbrains.persist.model.Category;
import ru.geekbrains.persist.model.Picture;
import ru.geekbrains.persist.model.Product;
import ru.geekbrains.persist.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
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
        productRepository = mock(ProductRepository.class);
        productService = new ProductServiceImpl(productRepository);
    }

    @Test
    public void testFindById() {

        Product expectedProduct = new Product();
        expectedProduct.setId(1L);
        expectedProduct.setTitle("LG 27UP850");
        expectedProduct.setCost(new BigDecimal(500));
        expectedProduct.setDescription("4k Monitor");
        expectedProduct.setCategory(new Category(1L, "Monitor"));
        expectedProduct.setBrand(new Brand(1L, "LG"));
        Picture pic1 = new Picture(1L, "pic1.jpg", "", UUID.randomUUID().toString(), expectedProduct);
        Picture pic2 = new Picture(2L, "pic2.jpg", "", UUID.randomUUID().toString(), expectedProduct);
        expectedProduct.setPictures(List.of(pic1, pic2));


        when(productRepository.findById(eq(expectedProduct.getId())))
                .thenReturn(Optional.of(expectedProduct));

        ProductDto productDto = productService.findById(expectedProduct.getId());

        assertEquals(expectedProduct.getId(), productDto.getId());
        assertEquals(expectedProduct.getTitle(), productDto.getTitle());
        assertEquals(expectedProduct.getCost(), productDto.getCost());
        assertEquals(expectedProduct.getDescription(), productDto.getDescription());
        assertEquals(expectedProduct.getCategory().getId(), productDto.getCategoryDto().getId());
        assertEquals(expectedProduct.getCategory().getTitle(), productDto.getCategoryDto().getTitle());
        assertEquals(expectedProduct.getBrand().getId(), productDto.getBrandDto().getId());
        assertEquals(expectedProduct.getBrand().getTitle(), productDto.getBrandDto().getTitle());

        assertNotNull(expectedProduct.getPictures());
        assertEquals(2, expectedProduct.getPictures().size());
    }

    @Test
    public void testFindWithFilter() {
        Product expectedProduct1 = new Product();
        expectedProduct1.setId(1L);
        expectedProduct1.setTitle("LG 27UP850");
        expectedProduct1.setCost(new BigDecimal(500));
        expectedProduct1.setDescription("4k Monitor");
        expectedProduct1.setCategory(new Category(1L, "Monitor"));
        expectedProduct1.setBrand(new Brand(1L, "LG"));

        Product expectedProduct2 = new Product();
        expectedProduct2.setId(2L);
        expectedProduct2.setTitle("MacBook Pro 13");
        expectedProduct2.setCost(new BigDecimal(1300));
        expectedProduct2.setCategory(new Category(2L, "Laptop"));
        expectedProduct2.setBrand(new Brand(2L, "Apple"));

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
