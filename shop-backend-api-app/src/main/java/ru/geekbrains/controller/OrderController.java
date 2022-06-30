package ru.geekbrains.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import ru.geekbrains.dto.OrderDetailDto;
import ru.geekbrains.dto.OrderDto;
import ru.geekbrains.persist.Order;
import ru.geekbrains.service.OrderService;

import java.util.List;
import java.util.UUID;

@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/own")
    public List<OrderDto> getUserOrders(Authentication authentication) {
        return orderService.getOrders(authentication.getName());
    }

    @GetMapping("/{id}")
    public List<OrderDetailDto> getOrderDetails(@PathVariable("id") UUID id) {
        return orderService.getOrderDetails(id);
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    public void createOrder(Authentication authentication) {
    	orderService.save(authentication.getName());
    }

    @PutMapping(produces = "application/json", consumes = "application/json")
    public List<OrderDetailDto> editOrderDetail(@RequestBody OrderDetailDto orderDetailDto) {
        return orderService.editOrderDetail(orderDetailDto);
    }

    @DeleteMapping("/{id}")
    public List<OrderDto> removeOrder(@PathVariable("id") UUID id, Authentication authentication) {
        orderService.removeOrder(id);
        return orderService.getOrders(authentication.getName());
    }

    @DeleteMapping("/detail/{id}")
    public List<OrderDetailDto> removeOrderDetail(@PathVariable("id") UUID id) {
        Order order = orderService.removeOrderDetail(id);
        return orderService.getOrderDetails(order.getId());
    }
}
