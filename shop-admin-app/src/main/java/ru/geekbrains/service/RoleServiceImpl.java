package ru.geekbrains.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.geekbrains.controller.dto.RoleDto;
import ru.geekbrains.persist.model.Role;
import ru.geekbrains.persist.repository.RoleRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    private RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<RoleDto> findAllRoles() {
        return roleRepository.findAll().stream()
                .map(role -> new RoleDto(role.getId(), role.getRole()))
                .collect(Collectors.toList());
    }
}
