package ru.geekbrains.persist.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.geekbrains.persist.model.OrderDetail;

import java.util.Optional;
import java.util.UUID;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, UUID> {

    @EntityGraph("orderDetailWithOrderEntityGraph")
    Optional<OrderDetail> findById(UUID id);
}
