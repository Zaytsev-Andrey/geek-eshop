package ru.geekbrains.persist;

import org.springframework.data.jpa.domain.Specification;

public class CategorySpecification {

    public static Specification<Category> titlePrefix(String prefix) {
        return ((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(root.get("title"), prefix + "%"));
    }
}
