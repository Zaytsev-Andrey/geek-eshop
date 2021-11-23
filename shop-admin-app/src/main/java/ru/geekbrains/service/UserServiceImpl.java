package ru.geekbrains.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.geekbrains.controller.dto.RoleDto;
import ru.geekbrains.controller.dto.UserDto;
import ru.geekbrains.controller.exception.EntityNotFoundException;
import ru.geekbrains.controller.param.UserListParam;
import ru.geekbrains.persist.model.Role;
import ru.geekbrains.persist.model.User;
import ru.geekbrains.persist.repository.UserRepository;
import ru.geekbrains.persist.specification.UserSpecification;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDto findUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(id, "User not found"));
        return new UserDto(user.getId(),
                user.getFirstname(),
                user.getLastname(),
                user.getEmail(),
                user.getRoles().stream()
                        .map(role -> new RoleDto(role.getId(), role.getRole()))
                        .collect(Collectors.toList()));
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Page<User> findUsersWithFilter(UserListParam listParam) {
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
    public void saveUser(UserDto userDto) {
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
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }
}
