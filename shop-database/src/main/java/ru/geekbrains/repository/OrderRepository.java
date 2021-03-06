package ru.geekbrains.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.geekbrains.persist.Order;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    @EntityGraph("orderWithDetailsEntityGraph")
    Optional<Order> findById(UUID id);

}
