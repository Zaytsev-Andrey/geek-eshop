package ru.geekbrains.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.geekbrains.dto.OrderDto;
import ru.geekbrains.exception.UserNotFoundException;
import ru.geekbrains.persist.User;
import ru.geekbrains.repository.UserRepository;

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

//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy 'at' HH:mm:ss");

        return currentUser.getOrders().stream()
                .map(order -> OrderDto.fromOrder(order))
                .collect(Collectors.toList());
    }
}
