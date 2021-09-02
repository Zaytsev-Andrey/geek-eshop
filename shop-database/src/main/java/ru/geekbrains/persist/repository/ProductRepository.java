package ru.geekbrains.persist.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.geekbrains.persist.model.Product;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    @Query(value = "select p from Product p left join fetch p.category left join fetch p.brand",
            countQuery = "select count(p) from Product p")
    Page<Product> findAll(Specification<Product> specification, Pageable var2);

    @Query(value = "select p from Product p " +
            "left join fetch p.pictures " +
            "left join fetch p.category " +
            "left join fetch p.brand " +
            "where p.id = :id")
    Optional<Product> findById(@Param("id") Long id);
}
