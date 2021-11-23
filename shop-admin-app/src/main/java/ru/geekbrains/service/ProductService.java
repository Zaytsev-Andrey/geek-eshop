package ru.geekbrains.service;

import org.springframework.data.domain.Page;
import ru.geekbrains.controller.dto.CategoryDto;
import ru.geekbrains.controller.dto.ProductDto;
import ru.geekbrains.controller.param.ProductListParam;
import ru.geekbrains.persist.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    ProductDto findProductById(Long id);

    Page<Product> findProductsWithFilter(ProductListParam listParam);

    void saveProduct(ProductDto productDto);

    void deleteProductById(Long id);
}
