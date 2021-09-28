package ru.geekbrains.service;

import javassist.NotFoundException;
import ru.geekbrains.controller.dto.OrderDto;

import java.util.List;

public interface UserService {

    public List<OrderDto> gerUserOrders(String email) throws NotFoundException;
}
