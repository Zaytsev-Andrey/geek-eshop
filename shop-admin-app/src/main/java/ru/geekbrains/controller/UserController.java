package ru.geekbrains.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.geekbrains.persist.User;
import ru.geekbrains.service.RoleService;
import ru.geekbrains.service.UserService;

import javax.validation.Valid;
import java.util.stream.Collectors;

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

    @GetMapping
    public String userList(Model model, UserListParam userListParams) {
        logger.info("User list page requested");

        model.addAttribute("users", userService.findWithFilter(userListParams));
        return "users";
    }

    @GetMapping("/new")
    public String newUserForm(Model model) {
        logger.info("Change user page requested");

        model.addAttribute("userDto", new UserDto());
        addListRoleToModel(model);
        return "user_form";
    }

    @GetMapping("/{id}")
    public String editUser(@PathVariable("id") Long id, Model model) {
        logger.info("Editing user");

        User currentUser = userService.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
        model.addAttribute("userDto", new UserDto(currentUser.getId(),
                currentUser.getFirstname(),
                currentUser.getLastname(),
                currentUser.getEmail(),
                currentUser.getRoles().stream()
                        .map(role -> new RoleDto(role.getId(), role.getRole()))
                        .collect(Collectors.toList())));
        addListRoleToModel(model);

        return "user_form";
    }



    @PostMapping
    public String update(@Valid UserDto userDto, BindingResult result, Model model) {
        logger.info("Updating user");

        if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
            result.rejectValue("password", "", "Password and confirm password do not equals");
            addListRoleToModel(model);
            return "user_form";
        }

        if (result.hasErrors()) {
            addListRoleToModel(model);
            return "user_form";
        }

        try {
            userService.save(userDto);
        } catch (Exception e) {
            result.rejectValue("email", "", String.format("User with E-mail: %s already exists",
                    userDto.getEmail()));
            addListRoleToModel(model);
            return "user_form";
        }

        return "redirect:/user";
    }

    @DeleteMapping("/{id}")
    public String removeUser(@PathVariable("id") Long id, Model model) {
        logger.info("Deleting user");

        userService.deleteById(id);

        return "redirect:/user";
    }

    private void addListRoleToModel(Model model) {
        model.addAttribute("roles", roleService.findAll().stream()
                .map(role -> new RoleDto(role.getId(), role.getRole()))
                .collect(Collectors.toSet()));
    }

    @ExceptionHandler
    public ModelAndView notFoundExceptionHandler(NotFoundException e) {
        ModelAndView modelAndView = new ModelAndView("not_found");
        modelAndView.addObject("message", e.getMessage());
        modelAndView.setStatus(HttpStatus.NOT_FOUND);
        return modelAndView;
    }
}
