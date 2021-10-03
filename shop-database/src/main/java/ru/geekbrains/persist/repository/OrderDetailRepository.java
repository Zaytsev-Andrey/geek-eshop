package ru.geekbrains.persist.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.geekbrains.persist.model.OrderDetail;

import java.util.Optional;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

    @EntityGraph("orderDetailWithOrderEntityGraph")
    Optional<OrderDetail> findById(Long id);
}
