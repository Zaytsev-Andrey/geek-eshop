package ru.geekbrains.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.geekbrains.controller.dto.OrderDetailDto;
import ru.geekbrains.controller.dto.OrderDto;
import ru.geekbrains.persist.Order;
import ru.geekbrains.persist.OrderStatus;
import ru.geekbrains.persist.User;
import ru.geekbrains.repository.UserRepository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
        expectedOrder.setId(1L);
        expectedOrder.setCreationDate(new Date());
        expectedOrder.setPrice(new BigDecimal(500));
        expectedOrder.setStatus(OrderStatus.CREATED);

        User expectedUser = new User();
        expectedUser.setId(1L);
        expectedUser.setEmail("user@mail.ru");
        expectedUser.setOrders(List.of(expectedOrder));

        when(userRepository.findUserByEmail(expectedUser.getEmail()))
                .thenReturn(Optional.of(expectedUser));

        List<OrderDto> dtoOrders = userService.getUserOrders(expectedUser.getEmail());

        assertNotNull(dtoOrders);
        assertEquals(1, dtoOrders.size());

        OrderDto orderDto = dtoOrders.get(0);
        assertEquals(expectedOrder.getId(), orderDto.getId());
        assertEquals(expectedOrder.getPrice(), orderDto.getPrice());
        assertEquals(expectedOrder.getStatus().getName(), orderDto.getStatus());
    }
}
