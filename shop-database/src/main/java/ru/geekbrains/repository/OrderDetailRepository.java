package ru.geekbrains.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.geekbrains.persist.OrderDetail;

import java.util.Optional;
import java.util.UUID;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, UUID> {

    @EntityGraph("orderDetailWithOrderEntityGraph")
    Optional<OrderDetail> findById(UUID id);
}
