package ru.geekbrains.controller.param;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrandListParam {

    private String titleFilter;
    private Integer page;
    private Integer size;
    private String sortField;
    private String sortDirection;
}
