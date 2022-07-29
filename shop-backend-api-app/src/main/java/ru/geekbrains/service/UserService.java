package ru.geekbrains.service;

import ru.geekbrains.persist.Order;
import java.util.Set;

public interface UserService {

    Set<Order> getUserOrders(String email);
}
