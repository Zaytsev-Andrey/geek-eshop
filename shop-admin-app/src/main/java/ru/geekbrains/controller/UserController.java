package ru.geekbrains.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.geekbrains.controller.dto.RoleDto;
import ru.geekbrains.controller.dto.UserDto;
import ru.geekbrains.controller.param.UserListParam;
import ru.geekbrains.service.RoleService;
import ru.geekbrains.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    private UserService userService;

    private RoleService roleService;

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
        model.addAttribute("users", userService.findUsersWithFilter(userListParams));
        return "users";
    }

    @GetMapping("/new")
    public String initNewUserForm(Model model) {
        logger.info("Initialization form to create new user");
        model.addAttribute("userDto", new UserDto());
        return "user_form";
    }

    @GetMapping("/{id}")
    public String initEditUserForm(@PathVariable("id") Long id, Model model) {
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
        boolean emailError = userService.findUserByEmail(userDto.getEmail()).isPresent();

        if (result.hasErrors() || passwordError || emailError) {
            if (passwordError) {
                result.rejectValue("password", "",
                        "Password and confirm password are not equals");
            }
            if (emailError) {
                result.rejectValue("email", "", String.format("User with E-mail: %s already exists",
                        userDto.getEmail()));
            }
            return "user_form";
        }

        userService.saveUser(userDto);

        return "redirect:/user";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        logger.info("Deleting user with id='{}'", id);
        userService.deleteUserById(id);
        return "redirect:/user";
    }

}
