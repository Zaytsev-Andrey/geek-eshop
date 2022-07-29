package ru.geekbrains.specification;

import org.springframework.data.jpa.domain.Specification;
import ru.geekbrains.persist.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


public class ProductSpecification {

    public static Specification<Product> titlePrefix(String prefix) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(root.get("title"), prefix + "%");
    }

    public static Specification<Product> titleLike(String substring) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(root.get("title"), "%" + substring + "%");
    }

    public static Specification<Product> categoryId(Long categoryId) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("category").get("id"), categoryId);
    }

    public static Specification<Product> categoryContains(List<UUID> categoriesId) {
        return (root, criteriaQuery, criteriaBuilder) ->
                root.get("category").get("id").in(categoriesId);
    }

    public static Specification<Product> brandId(Long brandId) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("brand").get("id"), brandId);
    }

    public static Specification<Product> minCost(BigDecimal minCost) {
        return ((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.ge(root.get("cost"), minCost));
    }

    public static Specification<Product> maxCost(BigDecimal maxCost) {
        return ((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.le(root.get("cost"), maxCost));
    }
}
