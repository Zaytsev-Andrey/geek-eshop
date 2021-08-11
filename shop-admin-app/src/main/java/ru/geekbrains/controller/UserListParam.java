package ru.geekbrains.controller;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserListParam {

    private String firstnameFilter;
    private String lastnameFilter;
    private String emailFilter;
    private Integer page;
    private Integer size;
    private String sortField;
    private String sortDirection;
}
