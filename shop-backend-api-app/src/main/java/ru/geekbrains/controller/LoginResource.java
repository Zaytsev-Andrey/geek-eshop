package ru.geekbrains.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class LoginResource {

    @GetMapping("/login")
    public User login(Authentication authentication) {
        return (User) authentication.getPrincipal();
    }
}
