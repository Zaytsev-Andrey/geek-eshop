package ru.geekbrains.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.geekbrains.dto.RoleDto;
import ru.geekbrains.dto.UserDto;
import ru.geekbrains.controller.param.UserListParam;
import ru.geekbrains.persist.User;
import ru.geekbrains.service.RoleService;
import ru.geekbrains.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/user")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    private final RoleService roleService;

    @Autowired
    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    /**
     * Load before each request method for loading list of roles to model
     * @return list of user roles
     */
    @ModelAttribute("roles")
    public List<RoleDto> loadUserRoles() {
        logger.info("Loading roles to model");
        return roleService.findAllRoles();
    }

    @GetMapping
    public String showUserListWithPaginationAndFilter(Model model, UserListParam userListParams) {
        logger.info("Getting page of users with filter");
        model.addAttribute("userDtos", userService.findUsersWithFilter(userListParams));
        return "users";
    }

    @GetMapping("/new")
    public String initNewUserForm(Model model) {
        logger.info("Initialization form to create new user");
        model.addAttribute("userDto", new UserDto());
        return "user_form";
    }

    @GetMapping("/{id}")
    public String initEditUserForm(@PathVariable("id") UUID id, Model model) {
        logger.info("Editing user with id='{}'", id);
        model.addAttribute("userDto", userService.findUserById(id));
        return "user_form";
    }

    @PostMapping
    public String saveUser(@Valid UserDto userDto, BindingResult result) {
        logger.info("Saving user with login='{}'", userDto.getEmail());

        // if password not equals confirm password
        boolean passwordError = !userDto.getPassword().equals(userDto.getConfirmPassword());
        // if E-mail already exist
        boolean emailError = false;
        Optional<User> existUser = userService.findUserByEmail(userDto.getEmail());
        if (existUser.isPresent() && !existUser.get().getId().toString().equals(userDto.getId())) {
            emailError = true;
        }

        if (result.hasErrors() || passwordError || emailError && userDto.getId().isBlank()) {
            if (passwordError) {
                result.rejectValue("password", "",
                        "Password and confirm password are not equals");
            }
            if (emailError && userDto.getId().isBlank()) {
                result.rejectValue("email", "", String.format("User with E-mail: %s already exists",
                        userDto.getEmail()));
            }
            return "user_form";
        }

        userService.saveUser(userDto);

        return "redirect:/user";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") UUID id) {
        logger.info("Deleting user with id='{}'", id);
        userService.deleteUserById(id);
        return "redirect:/user";
    }

}
