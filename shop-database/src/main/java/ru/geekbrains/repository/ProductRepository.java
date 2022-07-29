package ru.geekbrains.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.geekbrains.persist.Product;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {

    @EntityGraph("productAllEntityGraph")
    Page<Product> findAll(Specification<Product> specification, Pageable var2);

    @EntityGraph("productByIdEntityGraph")
    Optional<Product> findById(UUID id);
    
    @EntityGraph("productByIdWithPicturesEntityGraph")
    Optional<Product> findProductById(UUID id);

    void deleteById(UUID id);
    
    List<Product> findProductByIdIn(Set<UUID> ids);
}
