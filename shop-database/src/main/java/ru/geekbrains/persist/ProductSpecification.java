package ru.geekbrains.persist;

import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;


public class ProductSpecification {

    public static Specification<Product> titlePrefix(String prefix) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(root.get("title"), prefix + "%");
    }

    public static Specification<Product> categoryPrefix(String prefix) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.like(root.get("category").get("title"), prefix + "%");
    }

    public static Specification<Product> minCost(BigDecimal minCost) {
        return ((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.ge(root.get("cost"), minCost));
    }

    public static Specification<Product> maxCost(BigDecimal maxCost) {
        return ((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.le(root.get("cost"), maxCost));
    }
}
