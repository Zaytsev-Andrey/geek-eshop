package ru.geekbrains.service;

import ru.geekbrains.dto.RoleDto;
import ru.geekbrains.persist.Role;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface RoleService {

    List<RoleDto> findAllRoles();

    Set<Role> findRoleByIds(Set<UUID> ids);
}
