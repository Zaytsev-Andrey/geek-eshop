package ru.geekbrains.service;

import org.springframework.data.domain.Page;
import ru.geekbrains.dto.ProductDto;
import ru.geekbrains.controller.param.ProductListParam;

import java.util.UUID;

public interface ProductService {

    ProductDto findProductById(UUID id);

    Page<ProductDto> findProductsWithFilter(ProductListParam listParam);

    void saveProduct(ProductDto productDto);

    void deleteProductById(UUID id);
}
