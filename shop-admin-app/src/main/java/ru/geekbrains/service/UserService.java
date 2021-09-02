package ru.geekbrains.service;

import org.springframework.data.domain.Page;
import ru.geekbrains.controller.dto.UserDto;
import ru.geekbrains.controller.param.UserListParam;
import ru.geekbrains.persist.model.User;

import java.util.Optional;


public interface UserService {

    Optional<User> findById(Long id);

    Page<User> findWithFilter(UserListParam listParam);

    void save(UserDto userDto);

    void deleteById(Long id);
}
