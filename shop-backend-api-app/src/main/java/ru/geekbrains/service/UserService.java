package ru.geekbrains.service;

import ru.geekbrains.dto.OrderDto;

import java.util.List;

public interface UserService {

    List<OrderDto> getUserOrders(String email);
}
