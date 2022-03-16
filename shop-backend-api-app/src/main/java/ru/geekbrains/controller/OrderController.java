package ru.geekbrains.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import ru.geekbrains.dto.AllCartDto;
import ru.geekbrains.dto.CartItemDto;
import ru.geekbrains.dto.OrderDetailDto;
import ru.geekbrains.dto.OrderDto;
import ru.geekbrains.persist.model.Order;
import ru.geekbrains.service.CartService;
import ru.geekbrains.service.OrderService;
import ru.geekbrains.service.UserService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("/order")
public class OrderController {

    private OrderService orderService;

    private CartService cartService;

    private UserService userService;

    @Autowired
    public OrderController(OrderService orderService, CartService cartService, UserService userService) {
        this.orderService = orderService;
        this.cartService = cartService;
        this.userService = userService;
    }

    @GetMapping("/own")
    public List<OrderDto> getUserOrders(Authentication authentication) {
        return userService.getUserOrders(authentication.getName());
    }

    @GetMapping("/{id}")
    public List<OrderDetailDto> getOrderDetails(@PathVariable("id") UUID id) {
        return orderService.getOrderDetails(id);
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    public AllCartDto createOrder(Authentication authentication) {
    	return orderService.save(authentication.getName());
//        if (!cartService.isEmpty()) {
//            orderService.save(cartService.getLineItems().stream()
//            		.map(CartItemDto::fromCartItem)
//            		.collect(Collectors.toList()),
//                    cartService.getSubTotal(),
//                    authentication.getName());
//            cartService.clear();
//        }
//
//        return cartService.getLineItems().stream()
//        		.map(CartItemDto::fromCartItem)
//        		.collect(Collectors.toList());
    }

    @PutMapping(produces = "application/json", consumes = "application/json")
    public List<OrderDetailDto> editOrderDetail(@RequestBody OrderDetailDto orderDetailDto) {
        Order order = orderService.editOrderDetail(orderDetailDto);

        return orderService.getOrderDetails(order.getId());
    }

    @DeleteMapping("/{id}")
    public List<OrderDto> removeOrder(@PathVariable("id") UUID id,
                                      Authentication authentication) {

        orderService.removeOrder(id);

        String email = authentication.getName();
        return userService.getUserOrders(email);
    }

    @DeleteMapping("/detail/{id}")
    public List<OrderDetailDto> removeOrderDetail(@PathVariable("id") UUID id) {
        Order order = orderService.removeOrderDetail(id);

        return orderService.getOrderDetails(order.getId());
    }
}
