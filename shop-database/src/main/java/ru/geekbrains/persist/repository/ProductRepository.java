package ru.geekbrains.persist.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.geekbrains.persist.model.Product;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    @EntityGraph("productAllEntityGraph")
    Page<Product> findAll(Specification<Product> specification, Pageable var2);

    @EntityGraph("productByIdEntityGraph")
    Optional<Product> findById(Long id);

}
