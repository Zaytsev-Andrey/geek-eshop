package ru.geekbrains.service;

import ru.geekbrains.dto.RoleDto;
import java.util.List;

public interface RoleService {

    List<RoleDto> findAllRoles();

}
