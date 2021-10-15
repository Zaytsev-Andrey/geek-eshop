package ru.geekbrains.service;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.geekbrains.controller.dto.OrderDto;
import ru.geekbrains.exception.UserNotFoundException;
import ru.geekbrains.persist.model.User;
import ru.geekbrains.persist.repository.UserRepository;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<OrderDto> getUserOrders(String email) {
        User currentUser = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy 'at' HH:mm:ss");

        return currentUser.getOrders().stream()
                .map(order -> new OrderDto(
                        order.getId(),
                        dateFormat.format(order.getCreationDate()),
                        order.getPrice(),
                        order.getStatus().getName()
                ))
                .collect(Collectors.toList());
    }
}
