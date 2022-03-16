package ru.geekbrains.service;

import org.springframework.data.domain.Page;
import ru.geekbrains.dto.UserDto;
import ru.geekbrains.controller.param.UserListParam;
import ru.geekbrains.persist.model.User;

import java.util.Optional;
import java.util.UUID;


public interface UserService {

    UserDto findUserById(UUID id);

    Optional<User> findUserByEmail(String email);

    Page<User> findUsersWithFilter(UserListParam listParam);

    void saveUser(UserDto userDto);

    void deleteUserById(UUID id);
}
