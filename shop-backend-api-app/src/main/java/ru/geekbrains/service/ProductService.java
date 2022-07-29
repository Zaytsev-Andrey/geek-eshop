package ru.geekbrains.service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Page;
import ru.geekbrains.dto.ProductDto;
import ru.geekbrains.persist.Product;
import ru.geekbrains.controller.param.ProductListParam;


public interface ProductService {

    ProductDto findById(UUID id);

    Page<ProductDto> findWithFilter(ProductListParam listParam);
    
    List<Product> findProductByIdIn(Set<UUID> ids);

}
