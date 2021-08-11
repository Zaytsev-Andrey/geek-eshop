package ru.geekbrains.service;

import ru.geekbrains.persist.Role;

import java.util.List;

public interface RoleService {

    List<Role> findAll();
}
