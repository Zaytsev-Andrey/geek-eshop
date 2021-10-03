package ru.geekbrains.service;

import javassist.NotFoundException;
import org.springframework.security.core.Authentication;
import ru.geekbrains.controller.dto.AllCartDto;
import ru.geekbrains.controller.dto.OrderDetailDto;
import ru.geekbrains.controller.dto.OrderDto;
import ru.geekbrains.persist.model.Order;
import ru.geekbrains.service.dto.LineItem;

import java.math.BigDecimal;
import java.util.List;

public interface OrderService {

    void save(List<LineItem> lineItems, BigDecimal total, String email) throws NotFoundException;

    List<OrderDetailDto> findOrderDetails(Long id) throws NotFoundException;

    void removeOrder(Long id);

    Order editOrderDetail(OrderDetailDto orderDetailDto) throws NotFoundException;

    Order removeOrderDetail(Long id) throws NotFoundException;
}
