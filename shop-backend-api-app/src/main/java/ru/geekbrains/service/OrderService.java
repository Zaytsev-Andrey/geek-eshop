package ru.geekbrains.service;

import ru.geekbrains.dto.AllCartDto;
import ru.geekbrains.dto.CartItemDto;
import ru.geekbrains.dto.OrderDetailDto;
import ru.geekbrains.persist.model.Order;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface OrderService {

    AllCartDto save(String email);

    List<OrderDetailDto> getOrderDetails(UUID id);

    void removeOrder(UUID id);

    Order editOrderDetail(OrderDetailDto orderDetailDto);

    Order removeOrderDetail(UUID id);
}
