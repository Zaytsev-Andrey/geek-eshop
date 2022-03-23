package ru.geekbrains.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.geekbrains.dto.UserDto;
import ru.geekbrains.controller.exception.EntityNotFoundException;
import ru.geekbrains.controller.param.UserListParam;
import ru.geekbrains.mapper.Mapper;
import ru.geekbrains.persist.User;
import ru.geekbrains.repository.UserRepository;
import ru.geekbrains.specification.UserSpecification;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    private RoleService roleService;

    private Mapper<User, UserDto> userMapper;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           RoleService roleService,
                           Mapper<User, UserDto> userMapper,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDto findUserById(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(id.toString(), "User not found"));
        return userMapper.toDto(user);
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Page<UserDto> findUsersWithFilter(UserListParam listParam) {
        Specification<User> specification = Specification.where(null);

        if (listParam.getFirstnameFilter() != null && !listParam.getFirstnameFilter().isBlank()) {
            specification = specification.and(UserSpecification.firstnamePrefix(listParam.getFirstnameFilter()));
        }
        if (listParam.getLastnameFilter() != null && !listParam.getLastnameFilter().isBlank()) {
            specification = specification.and(UserSpecification.lastnamePrefix(listParam.getLastnameFilter()));
        }
        if (listParam.getEmailFilter() != null && !listParam.getEmailFilter().isBlank()) {
            specification = specification.and(UserSpecification.emailPrefix(listParam.getEmailFilter()));
        }

        String sortField = "id";
        if (listParam.getSortField() != null && !listParam.getSortField().isBlank()) {
            sortField = listParam.getSortField();
        }

        Sort sort = Sort.by(sortField).ascending();
        if ("desc".equals(listParam.getSortDirection())) {
            sort = sort.descending();
        }

        return userRepository.findAll(specification,
                PageRequest.of(Optional.ofNullable(listParam.getPage()).orElse(1) - 1,
                        Optional.ofNullable(listParam.getSize()).orElse(5), sort))
                .map(userMapper::toDto);
    }

    @Override
    public void saveUser(UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUserById(UUID id) {
        userRepository.deleteById(id);
    }
}
