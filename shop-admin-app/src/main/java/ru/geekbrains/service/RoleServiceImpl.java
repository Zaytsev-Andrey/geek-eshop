package ru.geekbrains.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.geekbrains.dto.RoleDto;
import ru.geekbrains.mapper.Mapper;
import ru.geekbrains.persist.Role;
import ru.geekbrains.repository.RoleRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    private final Mapper<Role, RoleDto> roleMapper;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository, Mapper<Role, RoleDto> roleMapper) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    @Override
    public List<RoleDto> findAllRoles() {
        return roleRepository.findAll().stream()
                .map(roleMapper::toDto)
                .collect(Collectors.toList());
    }

}
