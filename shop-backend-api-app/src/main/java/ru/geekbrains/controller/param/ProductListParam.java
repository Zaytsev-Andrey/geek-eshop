package ru.geekbrains.controller.param;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class ProductListParam {

    private String titleFilter;
    private List<String> categoriesFilter;
    private Long brandFilter;
    private BigDecimal minCostFilter;
    private BigDecimal maxCostFilter;
    private Integer page;
    private Integer size;
    private String sortField;
    private String sortDirection;
}
