package ru.geekbrains.specification;

import org.springframework.data.jpa.domain.Specification;
import ru.geekbrains.persist.Brand;

public class BrandSpecification {

    public static Specification<Brand> titlePrefix(String prefix) {
        return ((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(root.get("title"), prefix + "%"));
    }
}
