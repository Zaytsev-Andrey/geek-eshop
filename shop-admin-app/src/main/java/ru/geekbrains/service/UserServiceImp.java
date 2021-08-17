package ru.geekbrains.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.geekbrains.controller.UserDto;
import ru.geekbrains.controller.UserListParam;
import ru.geekbrains.persist.*;
import ru.geekbrains.persist.model.Role;
import ru.geekbrains.persist.model.User;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImp implements UserService {

    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImp(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Page<User> findWithFilter(UserListParam listParam) {
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
                        Optional.ofNullable(listParam.getSize()).orElse(5), sort));
    }

    @Override
    public void save(UserDto userDto) {
        userRepository.save(new User(userDto.getId(),
                userDto.getFirstname(),
                userDto.getLastname(),
                userDto.getEmail(),
                passwordEncoder.encode(userDto.getPassword()),
                userDto.getRolesDto().stream()
                        .map(roleDto -> new Role(roleDto.getId(), roleDto.getRole()))
                        .collect(Collectors.toList())));
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}
