package ru.geekbrains.controller;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryListParam {

    private String titleFilter;
    private Integer page;
    private Integer size;
    private String sortField;
    private String sortDirection;
}
