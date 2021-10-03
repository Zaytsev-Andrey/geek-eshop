package ru.geekbrains.persist.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.geekbrains.persist.model.Order;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @EntityGraph("orderWithDetailsEntityGraph")
    Optional<Order> findById(Long id);

}
