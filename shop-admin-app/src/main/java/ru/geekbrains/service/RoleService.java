package ru.geekbrains.service;

import ru.geekbrains.controller.dto.RoleDto;
import ru.geekbrains.persist.model.Role;

import java.util.List;

public interface RoleService {

    List<RoleDto> findAllRoles();
}
