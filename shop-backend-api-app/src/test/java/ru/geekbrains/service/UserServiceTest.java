package ru.geekbrains.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.geekbrains.persist.Order;
import ru.geekbrains.persist.OrderStatus;
import ru.geekbrains.persist.User;
import ru.geekbrains.repository.UserRepository;

import java.math.BigDecimal;
import java.util.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    private UserService userService;

    private UserRepository userRepository;

    @BeforeEach
    public void init() {
        userRepository = mock(UserRepository.class);
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    public void testGetUserOrders() {
        Order expectedOrder = new Order();
        expectedOrder.setId(UUID.randomUUID());
        expectedOrder.setCreationDate(new Date());
        expectedOrder.setPrice(new BigDecimal(500));
        expectedOrder.setStatus(OrderStatus.CREATED);

        User expectedUser = new User();
        expectedUser.setId(UUID.randomUUID());
        expectedUser.setEmail("user@mail.ru");
        expectedUser.setOrders(Set.of(expectedOrder));

        when(userRepository.findUserByEmail(expectedUser.getEmail()))
                .thenReturn(Optional.of(expectedUser));

        Set<Order> orders = userService.getUserOrders(expectedUser.getEmail());

        assertNotNull(orders);
        assertEquals(1, orders.size());

        Order order = orders.iterator().next();
        assertEquals(expectedOrder.getId(), order.getId());
        assertEquals(expectedOrder.getPrice(), order.getPrice());
        assertEquals(expectedOrder.getStatus().getName(), order.getStatus().getName());
    }
}
