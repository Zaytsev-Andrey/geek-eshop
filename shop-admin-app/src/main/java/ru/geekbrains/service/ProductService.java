package ru.geekbrains.service;

import org.springframework.data.domain.Page;
import ru.geekbrains.controller.ProductDto;
import ru.geekbrains.controller.ProductListParam;
import ru.geekbrains.persist.model.Product;

import java.util.Optional;

public interface ProductService {

    Optional<Product> findById(Long id);

    Page<Product> findWithFilter(ProductListParam listParam);

    void save(ProductDto productDto);

    void deleteById(Long id);
}
