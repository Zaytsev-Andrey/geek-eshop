package ru.geekbrains.specification;

import org.springframework.data.jpa.domain.Specification;
import ru.geekbrains.persist.Category;

public class CategorySpecification {

    public static Specification<Category> titlePrefix(String prefix) {
        return ((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(root.get("title"), prefix + "%"));
    }
}
