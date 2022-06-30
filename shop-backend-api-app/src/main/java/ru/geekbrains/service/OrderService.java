package ru.geekbrains.service;

import ru.geekbrains.dto.OrderDetailDto;
import ru.geekbrains.dto.OrderDto;
import ru.geekbrains.persist.Order;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    void save(String email);

    List<OrderDto> getOrders(String username);

    List<OrderDetailDto> getOrderDetails(UUID id);

    void removeOrder(UUID id);

    List<OrderDetailDto> editOrderDetail(OrderDetailDto orderDetailDto);

    Order removeOrderDetail(UUID id);
}
