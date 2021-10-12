package ru.geekbrains.service;

import org.springframework.data.domain.Page;
import ru.geekbrains.controller.dto.ProductDto;
import ru.geekbrains.controller.param.ProductListParam;
import ru.geekbrains.persist.model.Product;

import java.util.Optional;

public interface ProductService {

    ProductDto findById(Long id);

    Page<ProductDto> findWithFilter(ProductListParam listParam);

}
