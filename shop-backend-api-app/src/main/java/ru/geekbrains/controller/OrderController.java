package ru.geekbrains.controller;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import ru.geekbrains.controller.dto.OrderDetailDto;
import ru.geekbrains.controller.dto.OrderDto;
import ru.geekbrains.persist.model.Order;
import ru.geekbrains.service.CartService;
import ru.geekbrains.service.OrderService;
import ru.geekbrains.service.UserService;
import ru.geekbrains.service.dto.LineItem;

import java.util.List;

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
    public List<OrderDto> findAll(Authentication authentication) throws NotFoundException {
        String email = ((User) authentication.getPrincipal()).getUsername();

        return userService.gerUserOrders(email);
    }

    @GetMapping("/{id}")
    public List<OrderDetailDto> findDetails(@PathVariable("id") Long id) throws NotFoundException {
        return orderService.findOrderDetails(id);
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    public List<LineItem> createOrder(Authentication authentication) throws NotFoundException {
        if (!cartService.isEmpty()) {
            orderService.save(cartService.getLineItems(),
                    cartService.getSubTotal(),
                    ((User) authentication.getPrincipal()).getUsername());
            cartService.clear();
        }

        return cartService.getLineItems();
    }

    @PutMapping(produces = "application/json", consumes = "application/json")
    public List<OrderDetailDto> editOrderDetail(@RequestBody OrderDetailDto orderDetailDto) throws NotFoundException {
        Order order = orderService.editOrderDetail(orderDetailDto);

        return orderService.findOrderDetails(order.getId());
    }

    @DeleteMapping("/{id}")
    public List<OrderDto> removeOrder(@PathVariable("id") Long id,
                                      Authentication authentication) throws NotFoundException {

        orderService.removeOrder(id);

        String email = ((User) authentication.getPrincipal()).getUsername();
        return userService.gerUserOrders(email);
    }

    @DeleteMapping("/detail/{id}")
    public List<OrderDetailDto> removeOrderDetail(@PathVariable("id") Long id) throws NotFoundException {
        Order order = orderService.removeOrderDetail(id);

        return orderService.findOrderDetails(order.getId());
    }
}
