package ru.geekbrains.service;

import org.springframework.data.domain.Page;
import ru.geekbrains.controller.UserDto;
import ru.geekbrains.controller.UserListParam;
import ru.geekbrains.persist.User;

import java.util.Optional;


public interface UserService {

    Optional<User> findById(Long id);

    Page<User> findWithFilter(UserListParam listParam);

    void save(UserDto userDto);

    void deleteById(Long id);
}
