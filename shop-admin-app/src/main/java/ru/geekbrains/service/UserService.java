package ru.geekbrains.service;

import org.springframework.data.domain.Page;
import ru.geekbrains.controller.dto.UserDto;
import ru.geekbrains.controller.param.UserListParam;
import ru.geekbrains.persist.model.User;

import java.util.Optional;


public interface UserService {

    UserDto findUserById(Long id);

    Optional<User> findUserByEmail(String email);

    Page<User> findUsersWithFilter(UserListParam listParam);

    void saveUser(UserDto userDto);

    void deleteUserById(Long id);
}
