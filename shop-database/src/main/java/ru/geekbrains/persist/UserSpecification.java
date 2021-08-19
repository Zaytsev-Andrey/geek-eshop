package ru.geekbrains.persist;

import org.springframework.data.jpa.domain.Specification;
import ru.geekbrains.persist.model.User;

public class UserSpecification {

    public static Specification<User> firstnamePrefix(String prefix) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(root.get("firstname"), prefix + "%");
    }

    public static Specification<User> lastnamePrefix(String prefix) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(root.get("lastname"), prefix + "%");
    }

    public static Specification<User> emailPrefix(String prefix) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(root.get("email"), prefix + "%");
    }
}
